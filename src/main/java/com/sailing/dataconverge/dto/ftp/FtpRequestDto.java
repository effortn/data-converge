package com.sailing.dataconverge.dto.ftp;

import lombok.Data;

/**
 * 中心写入区系统FTP服务器连接信息
 * create by en
 * at 2018/10/22 10:21
 **/
@Data
public class FtpRequestDto {
    
    /**
     * 消息类型(ftp)
     **/
    private String msgtype;

    /**
     * 36位的guid
     **/
    private String guid;

    /**
     * 写入ftp服务器数量
     **/
    private int ftpsl;

    /**
     * ftp地址，多个ftp服务器用逗号分隔
     **/
    private String ftpdz;

    /**
     * ftp端口，多个ftp服务器用逗号分隔
     **/
    private String ftpdk;

    /**
     * ftp写入用户名，多个ftp服务器用逗号分隔
     **/
    private String ftpyhm;
    
    /**
     * ftp写入密码，多个ftp服务器用逗号分隔
     **/
    private String ftpmm;

    /**
     * 写入topic的时间，时间戳，精确到毫秒
     **/
    private Long xrsj;

}
