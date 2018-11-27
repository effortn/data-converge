package com.sailing.dataconverge.data;

import com.sailing.dataconverge.utils.CommUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Queue 工具类
 * create by en
 * at 2018/10/22 14:15
 **/
@Slf4j
public class QueueUtils {

    /**
     * 初始化队列集合
     * @param topicNameList
     */
    public static Map<String, LinkedBlockingQueue<String>> initQueue(List<String> topicNameList) {
        Assert.isTrue(!CommUtils.isEmpty(topicNameList), "【Queue初始化】 topic 不能为空");
        Map<String, LinkedBlockingQueue<String>> queueMap = new HashMap<>();
        log.info("【Queue初始化】topic：{}", Arrays.toString(topicNameList.toArray()));
        Iterator<String> iterator = topicNameList.iterator();
        while (iterator.hasNext()) {
            String topicName = iterator.next();
            queueMap.put(topicName, new LinkedBlockingQueue());
        }
        return queueMap;
    }
    /**
     * 获取队列数据量
     * @param topicName     队列对应的 topicName
     * @return
     */
    public static int queueSize(String topicName, Map<String, LinkedBlockingQueue<String>> queueMap) {
        Assert.isTrue(queueMap.containsKey(topicName), "【Queue获取】topicName 不存在, topicName:" + topicName);
        LinkedBlockingQueue<String> queue = queueMap.get(topicName);
        return queue.size();
    }

    /**
     * 向队列中放入数据
     * @param topicName     队列对应的 topicName
     * @param json           数据
     * @return      放入成功：true   失败：false
     */
    public static boolean produceQueue(String topicName, String json, Map<String, LinkedBlockingQueue<String>> queueMap) {
        Assert.isTrue(queueMap.containsKey(topicName), "【Queue获取】topicName 不存在");
        LinkedBlockingQueue<String> queue = queueMap.get(topicName);
        try {
            queue.put(json);
            return true;
        } catch (InterruptedException e) {
            log.error("【Queue获取】报错，topicName：{}，", topicName, e);
            return false;
        }
    }

    /**
     * 从队列中获取数据
     * @param topicName     队列对应的 topicName
     * @return      获取成功：数据json     失败：null
     */
    public static String consumeQueue(String topicName, Map<String, LinkedBlockingQueue<String>> queueMap) {
        Assert.isTrue(queueMap.containsKey(topicName), "【Queue获取】topicName 不存在");
        LinkedBlockingQueue<String> queue = queueMap.get(topicName);
        try {
            return queue.take();
        } catch (InterruptedException e) {
            log.error("【Queue获取】报错，topicName：{}，", topicName, e);
            return null;
        }

    }
}
