package com.sailing.dataconverge.dto.sbzt;

import lombok.Data;

/**
 * 中心平台请求区系统获取运维状态
 * Create by en
 * at 2018/10/19 11:33
 **/
@Data
public class SbStateRequestDto {
    
    /**
     * 消息类型(state)
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
     * 设备编号，为空代表获取所有设备
     */
    private String sbdm;

    /**
     * 写入topic的时间，时间戳，精确到毫秒
     **/
    private Long xrsj;
    
    
}
