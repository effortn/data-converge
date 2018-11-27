package com.sailing.dataconverge.config;

import com.sailing.dataconverge.repository.SbLkhRepository;
import com.sailing.dataconverge.utils.SpringUtils;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * 项目常量（其中一些是通过配置加载）
 * create by en
 * at 2018/10/23 15:15
 **/
public final class ConvergeConstent {

    // 平台代码
    private static String PTDM;

    // 数据库和服务器的间隔时间 （服务器当前时间 - 数据库当前时间）
    private static long DB_SERVER_INTERVAL_TIME;

    // 号牌种类文件配置名称
    public static final String HPZL_CODE = "hpzl";

    // 号牌颜色文件配置名称
    public static final String HPYS_CODE = "hpys";

    // 车身颜色文件配置名称
    public static final String CSYS_CODE = "csys";

    // 车标类型文件配置名称
    public static final String CBLX_CODE = "cblx";

    // 车辆类型文件配置名称
    public static final String CLLX_CODE = "cllx";

    // 违法类型文件配置名称
    public static final String WFLX_CODE = "wflx";

    // 断面方向类型文件配置名称
    public static final String DMFX_CODE = "dmfx";

    // 违法图片路径格式
    public static final String WFTP_PATH_FORMAT = "%s/wf/%s/%s";

    // 图片ID 格式
    public static final String TPID_FORMAT = "%s%s%s";

    public static final DateTimeFormatter DATE_TIME_FORMATTER
            = DateTimeFormat.forPattern("yyyyMMddHHmmssSSS");

    public static final DateTimeFormatter DATE_TIME_FORMATTER_JGSJ
            = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

    public static final DateTimeFormatter DATE_TIME_FORMATTER_XRSJ
            = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS");

    public static void initConstent() {
        ConvergeConfig convergeConfig = (ConvergeConfig) SpringUtils.getBean(ConvergeConfig.class);
        SbLkhRepository sbLkhRepository = (SbLkhRepository) SpringUtils.getBean(SbLkhRepository.class);
        String dbDate = sbLkhRepository.querySysdate();
        long dbMillis = DATE_TIME_FORMATTER.parseDateTime(dbDate + "000").getMillis();
        DB_SERVER_INTERVAL_TIME = System.currentTimeMillis() - dbMillis;
        PTDM = convergeConfig.getPtdm();
    }

    public static String PTDM() {
        return PTDM;
    }

    public static void setDbServerIntervalTime(long dbServerIntervalTime) {
        DB_SERVER_INTERVAL_TIME = dbServerIntervalTime;
    }

    public static long DB_SERVER_INTERVAL_TIME() {
        return DB_SERVER_INTERVAL_TIME;
    }

}
