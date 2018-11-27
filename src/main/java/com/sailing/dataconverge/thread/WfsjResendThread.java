package com.sailing.dataconverge.thread;

import com.alibaba.fastjson.JSON;
import com.sailing.dataconverge.config.ConvergeConfig;
import com.sailing.dataconverge.config.TopicConsumeConfig;
import com.sailing.dataconverge.config.TopicProduceConfig;
import com.sailing.dataconverge.data.KafkaConsumeQueueData;
import com.sailing.dataconverge.data.KafkaProduceQueueData;
import com.sailing.dataconverge.dto.wfsj.WfsjResendRequestDto;
import com.sailing.dataconverge.dto.wfsj.WfsjResendResponseDto;
import com.sailing.dataconverge.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 违法数据补传线程
 * create by en
 * at 2018/10/22 17:13
 **/
@Slf4j
public class WfsjResendThread implements Runnable {

    private TopicConsumeConfig topicConsumeConfig;
    private TopicProduceConfig topicProduceConfig;

    private ConvergeConfig convergeConfig;

    private static ExecutorService resendExecutor =
            new ThreadPoolExecutor(3, 5, 3, TimeUnit.HOURS, new LinkedBlockingQueue<Runnable>());

    public WfsjResendThread() {
        this.topicConsumeConfig = (TopicConsumeConfig) SpringUtils.getBean(TopicConsumeConfig.class);
        this.convergeConfig = (ConvergeConfig) SpringUtils.getBean(ConvergeConfig.class);
        this.topicProduceConfig = (TopicProduceConfig) SpringUtils.getBean(TopicProduceConfig.class);
    }

    @Override
    public void run() {
        // 中心违法数据补传请求的topic name
        String fix = topicConsumeConfig.getFix();
        while (true) {
            try {
                // 1. 获取违法数据补传信息
                if (KafkaConsumeQueueData.queueSize(fix) == 0) {
                    Thread.sleep(3000);
                    continue;
                }
                String resendInfoJson = KafkaConsumeQueueData.consumeQueue(fix);
                WfsjResendRequestDto resendInfo = (WfsjResendRequestDto) JSON.parse(resendInfoJson);
                log.info("【历史违法数据补传】获取请求信息：{}", resendInfo);
                // 2. 回复中心补传请求
                WfsjResendResponseDto resendResponse = new WfsjResendResponseDto();
                BeanUtils.copyProperties(resendInfo, resendResponse);
                resendResponse.setXrsj(System.currentTimeMillis());
                resendResponse.setResult(0);
                String resendResponseJson = JSON.toJSONString(resendResponse);
                log.info("【历史违法数据补传】获取请求信息：{}，回复：{}", resendInfo, resendResponse);
                boolean b = KafkaProduceQueueData.produceQueue(topicProduceConfig.getFixFk(), resendResponseJson);
                if (!b) {
                    Thread.sleep(500);
                    b = KafkaProduceQueueData.produceQueue(topicProduceConfig.getFixFk(), resendResponseJson);
                }
                // 3. 开启补传数据线程
                if (convergeConfig.getPtdm().equals(resendInfo.getPtdm())) {
                    resendExecutor.execute(new WfsjSendThread(resendInfo.getStarttime(), resendInfo.getEndtime(), resendInfo.getSbdm(),
                            convergeConfig.getMaxInterval()));
                }
                Thread.sleep(500);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("【历史违法数据补传】报错！", e);
            }
        }
    }
}
