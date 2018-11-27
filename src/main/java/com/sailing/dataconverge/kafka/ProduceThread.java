package com.sailing.dataconverge.kafka;

import com.sailing.dataconverge.data.KafkaProduceQueueData;
import com.sailing.dataconverge.kafka.component.KafkaUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.io.UnsupportedEncodingException;

/**
 * kafka 消息生产线程
 * create by en
 * at 2018/10/22 13:37
 **/
@Slf4j
public class ProduceThread implements Runnable {

    /**
     * 对应的 topic name
     **/
    private String produceTopicName;

    /**
     * 上传错误时数据需要重新上传的 topic, 默认和 produceTopicName 相同
     **/
    private String errorTopicName;

    /**
     * 每次抓取 kafka 数据的间隔时间
     */
    private long sleepTime;

    long DEFAULT_SLEEP_TIME = 200;

    public ProduceThread(String produceTopicName) {
        this.produceTopicName = produceTopicName;
        this.errorTopicName = produceTopicName;
        this.sleepTime = DEFAULT_SLEEP_TIME;
    }

    public ProduceThread(String produceTopicName, String errorTopicName) {
        this.produceTopicName = produceTopicName;
        this.errorTopicName = errorTopicName;
        this.sleepTime = DEFAULT_SLEEP_TIME;
    }

    public ProduceThread(String produceTopicName, String errorTopicName, long sleepTime) {
        this.produceTopicName = produceTopicName;
        this.errorTopicName = errorTopicName;
        this.sleepTime = sleepTime;
    }

    @Override
    public void run() {
        // 1. 获取 kafka 生产者
        final Producer<String, byte[]> producer = KafkaUtils.getProducer();
        while (true) {
            try {
                // 2. 循环获取 topic 对应的队列数据
                int size = KafkaProduceQueueData.queueSize(produceTopicName);
                if (size == 0) {
                    Thread.sleep(sleepTime);
                    continue;
                }
                String json = KafkaProduceQueueData.consumeQueue(produceTopicName);
                if (json == null || "".equals(json)) {
                    log.debug("【topic发送消息】获取数据为空！topic:{}", produceTopicName);
                }
                // 3. 数据不为空，则发送消息
                log.info("【topic发送消息】topic:{},json:{}", produceTopicName, json);
                long startTime = System.currentTimeMillis();
                Integer key = json.hashCode();
                byte[] value = json.getBytes("UTF-8");
                producer.send(new ProducerRecord<String, byte[]>(produceTopicName, key + "", value),
                        new kafkaDataCallBack(key + "", value, startTime));
                Thread.sleep(sleepTime);
            } catch (Exception e) {
                log.error("【topic发送消息】发生异常，topic：{}", produceTopicName, e);
            }
        }
    }

    /**
     * kafka 上传数据回调函数
     * create by en
     * at 2018/10/22 14:54
     **/
    class kafkaDataCallBack implements Callback{

        private String key;
        private byte[] value;

        private long startTime;

        public kafkaDataCallBack(String key, byte[] value, long startTime) {
            this.key = key;
            this.value = value;
            this.startTime = startTime;
        }

        @Override
        public void onCompletion(RecordMetadata meta, Exception exception) {
            long elapsedTime = System.currentTimeMillis() - this.startTime;
            if(meta != null) {
                log.info(String.format("[kafka-product-callback]：(%s, %s) send to partition(%s), topic(%s), offset(%s) in %s ms", this.key, this.value,
                        meta.partition(), meta.topic(), meta.offset(), elapsedTime));
            }else if(exception != null){
                log.error("[kafka-product-callback]：发送消息到kafka时，发生异常，进入重新上传队列！", exception);
                try {
                    KafkaProduceQueueData.produceQueue(errorTopicName, new String(value, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    log.error("[kafka-product-callback]：字节流转换失败, value:{}", value);
                    KafkaProduceQueueData.produceQueue(errorTopicName, new String(value));
                }
            }
        }
    }
}
