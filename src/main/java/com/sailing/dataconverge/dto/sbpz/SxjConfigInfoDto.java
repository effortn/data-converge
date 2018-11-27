package com.sailing.dataconverge.dto.sbpz;

import lombok.Data;

/**
 * 摄像机配置信息
 * Create by en
 * at 2018/10/18 17:29
 **/
@Data
public class SxjConfigInfoDto {

    /**
     * 摄像机通道序号，
     * 在同一个主机中，每个摄像机的通道序号唯一
     **/
    private Integer sxjtdxh;

    /**
     * 摄像机IP地址
     */
    private String sxjipdz;

    /**
     * 摄像机流媒体地址，无则传0
     **/
    private Integer sxjlmtdz;
    
    /**
     * 摄像机对应车道数量
     **/
    private Integer sxjcds;

    /**
     * 摄像机抓拍类型，
     * 0：正向抓拍
     * 1：反向抓拍
     **/
    private Integer sxjzplx;

    /**
     * 摄像机所在断面描述
     */
    private String sxjdmms;

    /**
     * 摄像机的抓拍方向，
     * 101：东向西 
     * 102：西向东 
     * 103：南向北 
     * 104：北向南
     **/
    private Integer sxjzpfx;

    /**
     * 摄像机对应的车道编号，多个车道编号通过逗号间隔
     */
    private String sxjcd;
    
    

}