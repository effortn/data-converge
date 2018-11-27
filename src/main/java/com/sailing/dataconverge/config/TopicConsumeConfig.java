package com.sailing.dataconverge.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Topic 相关的配置信息（此程序作为这些topic的消费者）
 * create by en
 * at 2018/10/22 16:02
 **/
@Data
@Configuration
@ConfigurationProperties(prefix = "topic.consume")
public class TopicConsumeConfig {

    // 1. 违法数据
    /**
     * 实时违法回执数据topic
     **/
    private String wfrsp;

    /**
     * 历史补传违法回执数据topic
     **/
    private String wfrspLs;

    // 2. 中心检查-补传服务
    /**
     * 中心平台请求区系统补传数据topic	
     **/
    private String fix;

    // 3. 设备配置数据
    /**
     * 中心平台请求区系统获取设备配置数据topic
     **/
    private String deviceConfig;
    
    // 4. 设备运维状态数据
    /**
     * 中心平台请求区系统获取运维状态topic
     **/
    private String deviceStatus;

    // 5. FTP服务器连接信息数据
    /**
     * 中心写入区系统FTP服务器连接信息topic
     **/
    private String ftp;

    /**
     * 所有程序对应的消费者的topicName，用","连接
     */
    private String topics;

}
