package com.sailing.dataconverge.kafka;

import com.sailing.dataconverge.data.KafkaConsumeQueueData;
import com.sailing.dataconverge.kafka.comm.KafkaConsts;
import com.sailing.dataconverge.kafka.component.KafkaUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;

/**
 * kafka 消费者线程
 * create by en
 * at 2018/10/22 10:48
 **/
@Slf4j
public class ConsumeThread implements Runnable {

    /**
     * 此线程消费的topic
     **/
    private String consumerTopicName;

    /**
     * 每次抓取 kafka 数据的间隔时间
     */
    private long sleepTime;

    long DEFAULT_SLEEP_TIME = 500;

    public ConsumeThread(String consumerTopicName) {
        this.consumerTopicName = consumerTopicName;
        this.sleepTime = DEFAULT_SLEEP_TIME;
    }

    public ConsumeThread(String topicName, long sleepTime) {
        this.consumerTopicName = topicName;
        this.sleepTime = sleepTime;
    }

    @Override
    public void run() {
        // 1. 获取 kafka 中对应的 topic 的消费者 consumer
        final Consumer<String, byte[]> consumer = KafkaUtils.getConsumer(consumerTopicName);
        while(true) {
            try {
                // 2. 循环从 topic 中拉取数据
                ConsumerRecords<String, byte[]> records = consumer.poll(KafkaConsts.DEFAULT_POLL_TIME_OUT);
                //  数据为空，继续循环
                if(records == null) {
                    Thread.sleep(sleepTime);
                    continue;
                }
                // 3. 拉取的数据不为空，遍历数据，打印日志，并调用 KafkaConsumeQueueData 的生产方法，将数据放入对应的队列
                for (ConsumerRecord<String, byte[]> record : records) {
                    log.info(String.format("【kafka获取数据】：{topic=%s, timestamp=%s, partition=%s, "
                                    + "offset=%s, key=%s, value=%s}",
                            record.topic(), record.timestamp(), record.partition(), record.offset(),
                            record.key(), record.value()));

                    // 将数据放入对应的队列
                    String value = new String(record.value(), "UTF-8");
                    Boolean accept = KafkaConsumeQueueData.produceQueue(consumerTopicName, value);
                    while (!accept) {
                        accept = KafkaConsumeQueueData.produceQueue(consumerTopicName, value);
                    }
                }
                Thread.sleep(sleepTime);
            } catch (Exception e) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                log.error(String.format("[data-consumer]：消费异常!!"), e);
            }
        }
    }

}