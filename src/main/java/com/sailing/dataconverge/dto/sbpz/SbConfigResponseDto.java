package com.sailing.dataconverge.dto.sbpz;

import lombok.Data;

import java.util.List;

/**
 * 区系统回复中心平台获取设备配置数据
 * Create by en
 * at 2018/10/18 17:15
 **/
@Data
public class SbConfigResponseDto {

    /**
     * 消息类型(config)
     */
    private String msgtype;

    /**
     * 36位的guid，与请求guid一致
     */
    private String guid;

    /**
     * 区域系统代码
     */
    private String ptdm;

    /**
     * 0：成功接收指令
     * 1：失败接收指令
     * 如果返回1失败接收指令，后面的sbsl与sbpz字段可为空
     **/
    private Integer result;

    /**
     * 设备数量
     **/
    private Integer sbsl;

    /**
     * 设备配置
     */
    private List<SbConfigInfoDto> sbpz;
    
    /**
     * 写入topic的时间，时间戳，精确到毫秒
     **/
    private Long xrsj;
    
    
}
