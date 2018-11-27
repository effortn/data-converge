package com.sailing.dataconverge.dto.sbpz;

import lombok.Data;

import java.util.List;

/**
 * 设备配置信息
 * Create by en
 * at 2018/10/18 17:26
 **/
@Data
public class SbConfigInfoDto {
    
    /**
     * 设备编号
     */
    private String sbdm;

    /**
     * 主机设备IP地址
     */
    private String ipdz;

    /**
     * 设备地点描述
     */
    private String sbms;

    /**
     * 设备经度
     */
    private Double sbjd;
    
    /**
     * 设备维度
     */
    private Double sbwd;
    
    /**
     * 区系统代码
     */
    private String ptdm;

    /**
     * 设备所在的路口号
     **/
    private Integer lkh;

    /**
     * 设备主机的摄像机配置，
     * 一个设备主机包括多个摄像机配置
     */
    private List<SxjConfigInfoDto> sxjpz;
    
    
}
