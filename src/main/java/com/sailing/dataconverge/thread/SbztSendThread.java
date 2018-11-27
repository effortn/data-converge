package com.sailing.dataconverge.thread;

import com.alibaba.fastjson.JSON;
import com.sailing.dataconverge.config.TopicConsumeConfig;
import com.sailing.dataconverge.config.TopicProduceConfig;
import com.sailing.dataconverge.data.KafkaConsumeQueueData;
import com.sailing.dataconverge.data.KafkaProduceQueueData;
import com.sailing.dataconverge.dto.sbzt.SbStateInfoDto;
import com.sailing.dataconverge.dto.sbzt.SbStateRequestDto;
import com.sailing.dataconverge.dto.sbzt.SbStateResponseDto;
import com.sailing.dataconverge.service.ConvergeService;
import com.sailing.dataconverge.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * 设备状态数据上传线程
 * create by en
 * at 2018/10/22 17:14
 **/
@Slf4j
public class SbztSendThread implements Runnable {

    private TopicConsumeConfig topicConsumeConfig;

    private TopicProduceConfig topicProduceConfig;

    private ConvergeService convergeService;

    public SbztSendThread() {
        this.topicProduceConfig = (TopicProduceConfig) SpringUtils.getBean(TopicProduceConfig.class);
        this.topicConsumeConfig = (TopicConsumeConfig) SpringUtils.getBean(TopicConsumeConfig.class);
        this.convergeService = (ConvergeService) SpringUtils.getBean(ConvergeService.class);
    }

    @Override
    public void run() {
        // 1. 获取放置设备配置信息请求的topic name
        String deviceStatusTopicName = topicConsumeConfig.getDeviceStatus();
        log.info("【设备运维状态推送】线程开始，topic：{}", deviceStatusTopicName);
        while (true) {
            try {
                if (0 == KafkaConsumeQueueData.queueSize(deviceStatusTopicName)) {
                    Thread.sleep(3000);
                    continue;
                }
                // 2. 获取topic中数据，根据请求，查询数据库中的数据
                String deviceStatusRequestJson = KafkaConsumeQueueData.consumeQueue(deviceStatusTopicName);
                log.info("【设备运维状态推送】获取请求数据：{}", deviceStatusRequestJson);
                SbStateRequestDto deviceStateRequestDto = JSON.parseObject(deviceStatusRequestJson, SbStateRequestDto.class);
                // 3. 新建设备运维状态回复信息
                SbStateResponseDto sbStateResponseDto = new SbStateResponseDto();
                List<SbStateInfoDto> sbStateDtoList = convergeService.querySbState(deviceStateRequestDto.getSbdm());
                BeanUtils.copyProperties(deviceStateRequestDto, sbStateResponseDto);
                sbStateResponseDto.setSbywzt(sbStateDtoList);
                sbStateResponseDto.setXrsj(System.currentTimeMillis());
                log.info("【设备运维状态推送】数据开始推送：{}", sbStateResponseDto);
                KafkaProduceQueueData.produceQueue(deviceStatusTopicName, JSON.toJSONString(sbStateResponseDto));
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("【设备运维状态推送】推送数据报错！", e);
            }
        }
    }
}
