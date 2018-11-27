package com.sailing.dataconverge.dto.wfsj;

import lombok.Data;

/**
 * 区系统回复中心平台补传指令
 * Create by en
 * at 2018/10/18 17:09
 **/
@Data
public class WfsjResendResponseDto {
    
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
    private Integer bclx;

    /**
     * 设备编号
     */
    private String sbdm;
    
    /**
     * 1 违法数据补传请求
     **/
    private Integer type;

    /**
     * 0：成功接收指令
     * 1：失败接收指令
     **/
    private Integer result;

    /**
     * 写入topic的时间，时间戳，精确到毫秒
     **/
    private Long xrsj;
    
    
}
