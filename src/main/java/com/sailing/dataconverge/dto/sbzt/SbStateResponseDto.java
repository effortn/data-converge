package com.sailing.dataconverge.dto.sbzt;

import lombok.Data;

import java.util.List;

/**
 * 设备主机运维状态
 * create by en
 * at 2018/10/22 10:19
 **/
@Data
public class SbStateResponseDto {
    
    /**
     * 消息类型(state)
     **/
    private String msgtype;

    /**
     * 36位的guid，与请求guid一致
     **/
    private String guid;

    /**
     * 区域系统代码
     **/
    private String ptdm;

    /**
     * 多个设备主机运维状态
     **/
    private List<SbStateInfoDto> sbywzt;

    /**
     * 写入topic的时间，时间戳，精确到毫秒
     **/
    private Long xrsj;

}
