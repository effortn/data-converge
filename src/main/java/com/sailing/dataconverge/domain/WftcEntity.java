package com.sailing.dataconverge.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

/**
 * 违法停车表
 * create by en
 * at 2018/10/24 15:45
 **/
@Data
@Table(name = "B_KKQP_WFTC")
@Entity
public class WftcEntity {

    /**
     * 记录编号
     */
    @Id
    private String jlbh;

    /**
     * 号牌号码
     */
    private String hphm;

    /**
     * 号牌种类
     */
    private Integer hpzl;

    /**
     * 经过时间
     */
    private Timestamp jgsj;

    /**
     * 车辆速度
     */
    private Double clsd;

    /**
     * 最高限速
     */
    private Integer zgxs;

    /**
     * 最低限速
     */
    private Integer zdxs;

    /**
     * 车外廓长
     */
    private Integer cwkc;

    /**
     * 号牌颜色
     */
    private String hpys;

    /**
     * 车辆类型
     */
    private String cllx;

    /**
     * 记录类型
     */
    private String jllx;

    /**
     * 红灯时间
     */
    private Integer hdsj;

    /**
     * 标准位置代码
     */
    private String bzwzdm;

    /**
     * 设备编号
     */
    private String sbbh;

    /**
     * 车道编号
     */
    private Integer cdbh;

    /**
     * 方向编号
     */
    private String fxbh;

    /**
     * 图片数量
     */
    private Integer tpsl;

    /**
     * 车辆标记
     */
    private String clbj;

    /**
     *
     */
    private String shbj;
    private String zfbj;

    /**
     * 数据来源
     */
    private String sjly;

    /**
     *
     */
    private Integer cclx;
    private String tp1;
    private String tp2;
    private String tp3;
    private String tp4;
    private String bz;

    /**
     * 添加标记
     */
    private String tjbj;

    /**
     * 车身颜色
     */
    private Integer csys;

    /**
     * 记录类别
     */
    private Integer jllb;

    /**
     * 行驶方向
     */
    private Integer xsfx;

    /**
     *
     */
    private Integer csbj;
    private Integer yzys;
    private Integer tplx;

    /**
     * 写入时间
     */
    private Timestamp xrsj;

    /**
     * 下载状态 0未下载 1已下载
     */
    private String xzzt;

}
