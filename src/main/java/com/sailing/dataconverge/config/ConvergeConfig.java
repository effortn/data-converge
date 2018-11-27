package com.sailing.dataconverge.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * 项目配置
 * create by en
 * at 2018/10/23 15:06
 **/
@Data
@Configuration
@ConfigurationProperties("converge")
public class ConvergeConfig {

    /**
     * 平台代码
     */
    private String ptdm;

    /**
     * 违法数据查询间隔时间
     */
    private Long queryTime;

    /**
     * 过车抓拍数据的时间比当前系统时间差滞后超过指定的时间范围
     **/
    private long lagTime;

    /**
     * 当违法数据上传出现问题，线程执行滞后时，会导致数据一致不实时，因此需要一个滞后量来保证上传线程执行的实时性
     */
    private long newThreadLagTime;

    /**
     * 补传数据查询时间上限和下限的最大差
     **/
    private long maxInterval;

    /**
     * 代码转换文件配置集合
     */
    private Map<String, String> codeConvertMap;

}
