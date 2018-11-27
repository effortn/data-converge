package com.sailing.dataconverge.dto.sbpz;

import com.sailing.dataconverge.dto.XrsjInterface;
import lombok.Data;

/**
 * 中心平台请求区系统获取设备配置数据
 * Create by en
 * at 2018/10/18 17:12
 **/
@Data
public class SbConfigRequestDto {
    
    /**
     * 消息类型(config)
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
     * 设备编号，为空代表获取所有设备配置
     */
    private String sbdm;

    /**
     * 写入topic的时间，时间戳，精确到毫秒
     */
    private Long xrsj;
    
    
}
