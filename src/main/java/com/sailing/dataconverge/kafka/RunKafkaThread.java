package com.sailing.dataconverge.kafka;

import com.sailing.dataconverge.config.TopicConsumeConfig;
import com.sailing.dataconverge.config.TopicProduceConfig;
import com.sailing.dataconverge.data.KafkaConsumeQueueData;
import com.sailing.dataconverge.data.KafkaProduceQueueData;
import com.sailing.dataconverge.kafka.component.KafkaUtils;
import com.sailing.dataconverge.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.springframework.util.Assert;

import java.io.File;
import java.util.*;

/**
 * 启动并监控 kafka 消费和生产线程的运行
 * create by en
 * at 2018/10/29 10:04
 **/
@Slf4j
public class RunKafkaThread {

    private static Map<String, Thread> consumeThreadMap = new HashMap<>();
    private static Map<String, Thread> produceThreadMap = new HashMap<>();

    /**
     * 启动 kafka 消费和生产线程
     */
    public static void startKafkaThread() {
        log.info("【Kafka主线程】初始化开始！");
        //注意启动时候，需要设置下面3个参数，参考deploy/run.bat；或者打开下面的注释
        System.setProperty("config.dir", System.getProperty("user.dir") + File.separator + "config");
        System.setProperty("sdba.principal", "kafka_baoshan");
        System.setProperty("security.enable", "true");
//        System.setProperty("java.security.auth.login.config", "true");
        // 1、 获取生产消费对应的 topic
        TopicConsumeConfig topicConsumeConfig = (TopicConsumeConfig) SpringUtils.getBean(TopicConsumeConfig.class);
        TopicProduceConfig topicProduceConfig = (TopicProduceConfig) SpringUtils.getBean(TopicProduceConfig.class);
        String[] consumeTopicArray = topicConsumeConfig.getTopics().split(",");
        String[] produceTopicArray = topicProduceConfig.getTopics().split(",");

        KafkaProduceQueueData.init(Arrays.asList(produceTopicArray));
        KafkaConsumeQueueData.init(Arrays.asList(consumeTopicArray));

        // 2、 初始化消费者的 consumer
        Map<String, Consumer<String, byte[]>> consumerMap = KafkaUtils.initConsumerMap(topicConsumeConfig.getTopics());
        Assert.notNull(consumerMap, "【Kafka主线程】初始化失败，消费者为空！");

        // 3、 启动生产、消费线程
        for (int i = 0; i < consumeTopicArray.length; i++) {
            String consumerTopicName = consumeTopicArray[i];
            Thread consumeThread = new Thread(new ConsumeThread(consumerTopicName), consumerTopicName);
            consumeThread.start();
            consumeThreadMap.put(consumerTopicName, consumeThread);
        }

        for (int i = 0; i < produceTopicArray.length; i++) {
            String produceTopicName = produceTopicArray[i];
            Thread produceThread = new Thread(new ProduceThread(produceTopicName), produceTopicName);
            produceThread.start();
            produceThreadMap.put(produceTopicName, produceThread);
        }

        // 3、 启动定时任务，监控生产消费线程是否停止运行
        TimerTask kafkaMonitorTask = new TimerTask() {
            @Override
            public void run() {
                log.info("【Kafka主线程】开始检测kafka线程运行情况!");
                for (String key : consumeThreadMap.keySet()) {
                    Thread consumeThread = consumeThreadMap.get(key);
                    if (!consumeThread.isAlive()) {
                        log.error("【Kafka主线程】消费线程停止！topic：{}，准备重启！", key);
                        consumeThread.interrupt();
                        consumeThread = new Thread(new ConsumeThread(key), key);
                        consumeThread.start();
                    }
                }
                for (String key : produceThreadMap.keySet()) {
                    Thread produceThread = produceThreadMap.get(key);
                    if (!produceThread.isAlive()) {
                        log.error("【Kafka主线程】生产线程停止！topic：{}，准备重启！", key);
                        produceThread.interrupt();
                        produceThread = new Thread(new ConsumeThread(key), key);
                        produceThread.start();
                    }
                }
                log.info("【Kafka主线程】检测kafka线程运行情况完成!");
            }
        };
        new Timer().schedule(kafkaMonitorTask, 10 * 60 * 1000, 10 * 60 * 1000);
        log.info("【Kafka主线程】初始化完毕！");
    }

}
