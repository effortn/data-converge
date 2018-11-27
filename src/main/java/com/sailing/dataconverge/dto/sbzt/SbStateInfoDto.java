package com.sailing.dataconverge.dto.sbzt;

import lombok.Data;

import java.util.List;

/**
 * 区系统回复中心平台设备运维状态
 * create by en
 * at 2018/10/24 10:15
 **/
@Data
public class SbStateInfoDto {
    
    /**
     * 设备主机编号
     **/
    private String sbdm;

    /**
     * 设备IP地址
     **/
    private String ipdz;

    /**
     * 设备与区系统管理平台通信状态，
     * y：连接 
     * n：中断
     **/
    private String txzt;

    /**
     * 设备工作状态，
     * y：正常 
     * n：异常
     **/
    private String zt;

    /**
     * 设备硬盘容量使用率（0-100）
     **/
    private Integer ypsyl;

    /**
     * 设备cpu占有率百分比(0-100)
     **/
    private Integer cpuzyl;

    /**
     * 设备下多个摄像机运维状态
     **/
    private List<SxjStateInfoDto> sxjwzt;

}
