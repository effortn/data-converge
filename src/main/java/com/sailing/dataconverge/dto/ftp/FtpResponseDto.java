package com.sailing.dataconverge.dto.ftp;

import lombok.Data;

/**
 * 区系统回复中心FTP连接信息
 * create by en
 * at 2018/10/22 10:26
 **/
@Data
public class FtpResponseDto {
    
    /**
     * 消息类型(ftp)
     **/
    private String msgtype;

    /**
     * 36位的guid
     **/
    private String guid;

    /**
     * 0：接收失败
     * 1：接收成功
     **/
    private int jg;

    /**
     * 写入topic的时间，时间戳，精确到毫秒
     **/
    private Long xrsj;
    
    
}
