package com.sailing.dataconverge.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Topic 相关的配置信息（此程序作为这些topic的生产者）
 * create by en
 * at 2018/10/22 16:02
 **/
@Data
@Configuration
@ConfigurationProperties(prefix = "topic.produce")
public class TopicProduceConfig {

   // 1. 违法数据
    /**
     * 实时违法数据topic
     **/
    private String wf;

    /**
     * 历史补传违法数据topic
     **/
    private String wfLs;

    // 2. 中心检查-补传服务
    /**
     * 区系统回复中心平台补传指令topic
     **/
    private String fixFk;

    // 3. 设备配置数据
    /**
     * 区系统回复中心平台获取设备配置数据topic
     **/
    private String deviceConfigFk;
    
    // 4. 设备运维状态数据
    /**
     * 区系统回复中心平台设备运维状态topic
     **/
    private String deviceStatusFk;

    // 5. FTP服务器连接信息数据
    /**
     * 区系统回复中心FTP连接信息topic
     **/
    private String ftpFk;

    /**
     * 所有程序对应的生产者的topicName，用","连接
     */
    private String topics;

}
