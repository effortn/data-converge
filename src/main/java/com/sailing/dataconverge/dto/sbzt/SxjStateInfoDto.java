package com.sailing.dataconverge.dto.sbzt;

import lombok.Data;

/**
 * 摄像机运维状态
 * create by en
 * at 2018/10/22 10:19
 **/
@Data
public class SxjStateInfoDto {
    
    /**
     * 摄像机通道序号，
     * 在同一个设备中，每个摄像机的通道序号唯一
     **/
    private String sxjtdxh;
    
    /**
     * 摄像机IP地址
     **/
    private String sxjipdz;

    /**
     * 摄像机与设备的通信连接状态,
     * y：连接
     * n：中断
     * u：未知
     **/
    private String sxjtxzt;
    
    /**
     * 摄像机工作状态,
     * y：正常
     * n：异常
     **/
    private String sxjzt;

}
