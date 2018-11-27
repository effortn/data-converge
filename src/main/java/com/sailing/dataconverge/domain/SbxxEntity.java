package com.sailing.dataconverge.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 设备信息表
 * create by en
 * at 2018/10/24 16:47
 **/
@Data
@Table(name = "B_SSSB_SBXX")
@Entity
public class SbxxEntity {

    /**
     * 设备编号
     */
    @Id
    private String	sbbh;

    /**
     * 设备名称
     */
    private String	sbmc;

    /**
     * 设备类型
     */
    private String	sblx;

    /**
     *
     */
    private String	sbdl;

    /**
     * 设备描述
     */
    private String	sbms;

    /**
     * 标准位置代码
     */
    private String	bzwzdm;

    /**
     * 经度
     */
    private Double	jd;

    /**
     * 纬度
     */
    private Double	wd;
    
    /**
     * IP地址
     */
    private String	ipdz;

    /**
     *
     */
    private Double	tcxh;

    /**
     *
     */
    private String	dycvsbmc;
    private String	sbsmzt;
    private String	yhz;
    private String	dfsbdm;
    private String	glfwbh;

    /**
     * 预留字段1
     */
    private String	ylzd1;

    /**
     * 预留字段2
     */
    private String	ylzd2;

    /**
     * 预留字段3
     */
    private String	ylzd3;

    /**
     * 上级权限：0-可看;1-仅可查;2-不可见
     */
    private String	sjqx;
    private String	txfwbh;
    private String	ccfwbh;
    private String	ybh;
    private String	ljfs;
    private Double	bzlx;
    private Double	xh;

    private String	xwjxh;
    private String	sbxingh;
    private String	azrq;
    private Double	bxzq;
    private Double	byzq;
    private String	zjbyrq;
    private String	jdhgrq;
    private String	csmc;
    private String	lxfs;
    private String	sbtxfs;
    //private String	rjbb;

}
