package com.sailing.dataconverge.thread;

import com.alibaba.fastjson.JSON;
import com.sailing.dataconverge.config.ConvergeConstent;
import com.sailing.dataconverge.config.TopicConsumeConfig;
import com.sailing.dataconverge.config.TopicProduceConfig;
import com.sailing.dataconverge.data.KafkaConsumeQueueData;
import com.sailing.dataconverge.data.KafkaProduceQueueData;
import com.sailing.dataconverge.dto.sbpz.SbConfigRequestDto;
import com.sailing.dataconverge.dto.sbpz.SbConfigInfoDto;
import com.sailing.dataconverge.dto.sbpz.SbConfigResponseDto;
import com.sailing.dataconverge.service.ConvergeService;
import com.sailing.dataconverge.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 设备配置数据上传线程
 * create by en
 * at 2018/10/22 17:15
 **/
@Slf4j
public class SbpzSendThread implements Runnable {

    private TopicConsumeConfig topicConsumeConfig;

    private TopicProduceConfig topicProduceConfig;

    private ConvergeService convergeService;

    public SbpzSendThread() {
        this.topicProduceConfig = (TopicProduceConfig) SpringUtils.getBean(TopicProduceConfig.class);
        this.topicConsumeConfig = (TopicConsumeConfig) SpringUtils.getBean(TopicConsumeConfig.class);
        this.convergeService = (ConvergeService) SpringUtils.getBean(ConvergeService.class);
    }

    @Override
    public void run() {
        // 1. 获取放置设备配置信息请求的topic name
        String deviceConfigConsumeTopicName = topicConsumeConfig.getDeviceConfig();
        String deviceConfigFkTopicName = topicProduceConfig.getDeviceConfigFk();
        log.info("【设备配置信息推送】线程开始，topic：{}", deviceConfigConsumeTopicName);
        while (true) {
            try {
                if (0 == KafkaConsumeQueueData.queueSize(deviceConfigConsumeTopicName)) {
                    Thread.sleep(3000);
                    continue;
                }
                // 2. 获取topic中数据，根据请求，查询数据库中的数据
                String deviceConfigRequestJson = KafkaConsumeQueueData.consumeQueue(deviceConfigConsumeTopicName);
                log.info("【设备配置信息推送】获取请求数据：{}", deviceConfigRequestJson);
                SbConfigInfoDto deviceConfigRequestDto = JSON.parseObject(deviceConfigRequestJson, SbConfigInfoDto.class);
                // 3. 判断拿出的平台代码和本台代码是否一致
                SbConfigResponseDto sbConfigResponseDto;
                // 3-1. 不一致，返回接收失败
                if (!ConvergeConstent.PTDM().equals(deviceConfigRequestDto.getPtdm())) {
                    log.error("【设备配置信息推送】获取数据的平台代码不相配，获取平台代码：{}", deviceConfigRequestDto.getPtdm());
                    sbConfigResponseDto = new SbConfigResponseDto();
                    sbConfigResponseDto.setResult(0);
                    sbConfigResponseDto.setXrsj(System.currentTimeMillis());
                    log.info("【设备配置信息推送】数据开始推送：{}", sbConfigResponseDto);
                    KafkaProduceQueueData.produceQueue(deviceConfigFkTopicName, JSON.toJSONString(sbConfigResponseDto));
                    continue;
                }
                // 3-2. 代码一致，查询数据，推送消息
                sbConfigResponseDto = new SbConfigResponseDto();
                List<SbConfigInfoDto> sbConfigInfoDtos = convergeService.querySbConfig(deviceConfigRequestDto.getSbdm());
                BeanUtils.copyProperties(deviceConfigRequestDto, sbConfigResponseDto);
                sbConfigResponseDto.setSbpz(sbConfigInfoDtos);
                sbConfigResponseDto.setResult(1);
                sbConfigResponseDto.setSbsl(sbConfigInfoDtos.size());
                sbConfigResponseDto.setXrsj(System.currentTimeMillis());
                log.info("【设备配置信息推送】数据开始推送：{}", sbConfigResponseDto);
                KafkaProduceQueueData.produceQueue(deviceConfigFkTopicName, JSON.toJSONString(sbConfigResponseDto));
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("【设备配置信息推送】推送数据报错！", e);
            }
        }
    }
}
