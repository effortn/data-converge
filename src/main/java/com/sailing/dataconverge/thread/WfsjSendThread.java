package com.sailing.dataconverge.thread;

import com.alibaba.fastjson.JSON;
import com.sailing.dataconverge.DataConvergeApplication;
import com.sailing.dataconverge.config.ConvergeConfig;
import com.sailing.dataconverge.config.ConvergeConstent;
import com.sailing.dataconverge.config.TopicProduceConfig;
import com.sailing.dataconverge.data.KafkaProduceQueueData;
import com.sailing.dataconverge.dto.wfsj.WfsjDto;
import com.sailing.dataconverge.service.ConvergeService;
import com.sailing.dataconverge.utils.CommUtils;
import com.sailing.dataconverge.utils.SpringUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 违法数据上传线程
 * create by en
 * at 2018/10/22 16:35
 **/
@Data
@Slf4j
public class WfsjSendThread implements Runnable {

    /**
     * 查询违法数据的时间上限的初始值
     */
    private long beginTime;

    /**
     * 查询违法数据的结束时间，超过这个时间线程停止执行
     * stopTime = -1 时，这个线程不会停止
     */
    private long stopTime;

    /**
     * 设备代码
     **/
    private String sbdm;

    /**
     * 违法数据查询间隔时间
     **/
    private long queryTime;

    private TopicProduceConfig topicProduceConfig;

    private ConvergeService convergeService;

    private ConvergeConfig convergeConfig;

    public WfsjSendThread(long beginTime) {
        this.topicProduceConfig = (TopicProduceConfig) SpringUtils.getBean(TopicProduceConfig.class);
        this.convergeConfig = (ConvergeConfig) SpringUtils.getBean(ConvergeConfig.class);
        this.convergeService = (ConvergeService) SpringUtils.getBean(ConvergeService.class);
        this.beginTime = beginTime;
        this.sbdm = null;
        this.queryTime = convergeConfig.getQueryTime();
        this.stopTime = -1;
    }

    public WfsjSendThread(long beginTime, long stopTime, String sbdm, long queryTime) {
        this.topicProduceConfig = (TopicProduceConfig) SpringUtils.getBean(TopicProduceConfig.class);
        this.convergeConfig = (ConvergeConfig) SpringUtils.getBean(ConvergeConfig.class);
        this.convergeService = (ConvergeService) SpringUtils.getBean(ConvergeService.class);
        this.beginTime = beginTime;
        this.stopTime = stopTime;
        this.sbdm = sbdm;
        if (queryTime == -1) {
            this.queryTime = convergeConfig.getQueryTime();
        } else {
            this.queryTime = queryTime;
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                String beginTimeString = ConvergeConstent.DATE_TIME_FORMATTER.print(beginTime);
                log.info("【违法数据上传】本次上传开始，开始时间：{}", beginTimeString);
                // 1. 计算当前查询违法数据的下限时间
                long endTime = beginTime + queryTime;
                if (endTime >= stopTime && stopTime != -1) {
                    endTime = stopTime;
                }
                // 2. 判断下限时间是否大于数据库服务器时间，是则需要休眠一段时间才能继续查询
                if (endTime > System.currentTimeMillis() - ConvergeConstent.DB_SERVER_INTERVAL_TIME()) {
                    Thread.sleep(endTime - (System.currentTimeMillis() - ConvergeConstent.DB_SERVER_INTERVAL_TIME()));
                }
                // 3. 根据开始时间结束时间查询违法数据（设备代码为空，表示查询所有）
                List<WfsjDto> wfsjDtoList = convergeService.queryWfsj(sbdm, beginTime, endTime);
                if (CommUtils.isEmpty(wfsjDtoList)) {
                    beginTime = endTime;
                    continue;
                }
                log.info("【违法数据上传】本次上传开始，开始时间：{}，查询数据量：{}", beginTimeString, wfsjDtoList.size());
                // 4. 开始上传至违法数据 topic 对应的队列中
                wfsjDtoList.forEach(wfsjDto -> {
                    if(wfsjDto == null) {
                        return;
                    }
                    wfsjDto.setXrsj(ConvergeConstent.DATE_TIME_FORMATTER_XRSJ.print(System.currentTimeMillis()));
                    String jsonString = JSON.toJSONString(wfsjDto);
                    log.info("【违法数据上传】违反数据:{}", jsonString);
                    // 5. 如果过车抓拍数据的时间比当前系统时间差滞后超过指定的时间范围，上传至违法数据历史 topic 对应的队列
                    if (ConvergeConstent.DATE_TIME_FORMATTER_JGSJ.parseDateTime(wfsjDto.getJgsj()).getMillis() + convergeConfig.getLagTime() < System.currentTimeMillis()) {
                        KafkaProduceQueueData.produceQueue(topicProduceConfig.getWfLs(), jsonString);
                    } else {
                        //  否则上传至违法数据实时topic
                        KafkaProduceQueueData.produceQueue(topicProduceConfig.getWf(), jsonString);
                    }
                } );
                //  5. 判断线程是否需要结束，查询结束时间和停止
                if (endTime >= stopTime && stopTime != -1) {
                    log.info("【违法数据上传】此上传线程停止，开始时间：{}，停止时间：{}", beginTimeString, endTime);
                    break;
                }
                // 6. 判断是否需要新启实时上传违法的线程
                //  当此线程不会停止，且查询违法数据的滞后时间已经大于配置的阈值，则新建实时查询线程查询，此线程的终止时间为新线程的开始时间
                if(stopTime == -1 && endTime + convergeConfig.getNewThreadLagTime() <= System.currentTimeMillis()) {
                    stopTime = System.currentTimeMillis();
                    log.info("【违法数据上传】新建实时线程，开始时间：{}", ConvergeConstent.DATE_TIME_FORMATTER.print(stopTime));
                    DataConvergeApplication.wfsjSendExecutor.execute(new WfsjSendThread(stopTime));
                }
                // 7. 重置下次查询的开始时间
                beginTime = endTime;
                Thread.sleep(200);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("【违法数据上传】上传报错，开始时间：{}", e);
            }
        }
    }
}
