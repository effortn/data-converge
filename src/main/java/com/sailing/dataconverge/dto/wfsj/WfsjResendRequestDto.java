package com.sailing.dataconverge.dto.wfsj;

import lombok.Data;

/**
 * 中心平台请求区系统补传数据
 * Create by en
 * at 2018/10/18 16:27
 **/
@Data
public class WfsjResendRequestDto {
    
    /**
     * 消息类型(resend)
     */
    private String msgtype;
    
    /**
     * 36位的guid
     */
    private String guid;

    /**
     * 区域系统代码
     */
    private String ptdm;

    /**
     * 补传类型，
     * 1 设备级别补传（补传设备下所有的记录）
     */
    private String bclx;

    /**
     * 设备编号
     */
    private String sbdm;

    /**
     * 1违法数据补传请求
     */
    private Integer type;
    
    /**
     * 开始补传的经过时间，时间戳，精确到秒
     **/
    private Long starttime;
    
    /**
     * 结束补传的经过时间，时间戳，精确到秒
     **/
    private Long endtime;

    /**
     * 写入topic的时间，时间戳，精确到毫秒
     **/
    private Long xrsj;
    
}
