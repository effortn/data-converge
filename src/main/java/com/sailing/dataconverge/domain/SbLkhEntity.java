package com.sailing.dataconverge.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 设备路口对应关系
 * create by en
 * at 2018/10/25 16:14
 **/
@Data
@Table(name = "B_SSSB_SB_LKH")
@Entity
public class SbLkhEntity {

    /**
     * 设备编号
     **/
    @Id
    private String sbbh;

    /**
     * 路口号
     **/
    private String lkh;

    /**
     * 设备发送标志,上传"1"，不上传"0"
     **/
    private String sbfszt;
    

}
