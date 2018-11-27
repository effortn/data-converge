package com.sailing.dataconverge.dto.wfsj;

import lombok.Data;

/**
 * 违法数据回复
 * Create by en
 * at 2018/10/18 16:04
 **/
@Data
public class WfsjResponseDto {
    
    /**
     * 图片id，
     * 37位：设备编号（18位）+车道号（2位）+经过时间17位（yyyy-MM-dd HH:mm:ss:SSS 精确至毫秒）
     */
    private String tpid;

    /**
     * 写入结果，
     * 0：失败，1：成功
     */
    private String xrjg;

    /**
     * 写入时间：时间戳，精确到毫秒
     **/
    private Long xrsj;
    
    
}
