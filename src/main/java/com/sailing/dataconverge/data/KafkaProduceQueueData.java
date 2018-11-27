package com.sailing.dataconverge.data;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 存放要向 kafka 中生产的消息数据的队列数据
 * create by en
 * at 2018/10/22 13:46
 **/
@Slf4j
public class KafkaProduceQueueData {

    // 存放 kafka 中取出的数据的队列的集合， key 为 topicName
    private static Map<String, LinkedBlockingQueue<String>> queueMap;

    /**
     * 初始化队列集合
     * @param topicNameList
     */
    public static void init(List<String> topicNameList) {
        queueMap = QueueUtils.initQueue(topicNameList);
    }

    /**
     * 获取队列数据量
     * @param topicName     队列对应的 topicName
     * @return
     */
    public static int queueSize(String topicName) {
        return QueueUtils.queueSize(topicName, queueMap);
    }

    /**
     * 向队列中放入数据
     * @param topicName     队列对应的 topicName
     * @param json           数据
     * @return      放入成功：true   失败：false
     */
    public static boolean produceQueue(String topicName, String json) {
        return QueueUtils.produceQueue(topicName, json, queueMap);
    }

    /**
     * 从队列中获取数据
     * @param topicName     队列对应的 topicName
     * @return      获取成功：数据json     失败：null
     */
    public static String consumeQueue(String topicName) {
        return QueueUtils.consumeQueue(topicName, queueMap);
    }


}
