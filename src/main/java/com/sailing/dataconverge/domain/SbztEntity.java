package com.sailing.dataconverge.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "B_SSSB_SBDQZTXX")
public class SbztEntity {

    /**
     * 序号
     */
    @Id
    private Long	xh;

    /**
     * 更新时间
     */
    private String	gxsj;

    /**
     * 设备编号
     */
    private String	sbbh;

    /**
     * 设备状态类型（1-正常;2-故障;3-脱机）
     */
    private String	sbztlx;

    /**
     * 故障类型
     */
    private String	gzlx;

    /**
     * 故障描述
     */
    private String	gzms;

    // 预留字段1
    private String	ylzd1;

    // 预留字段2
    private String	ylzd2;

    // 预留字段3
    private String	ylzd3;

}
