package com.sailing.dataconverge.service;

import com.sailing.dataconverge.dto.sbpz.SbConfigInfoDto;
import com.sailing.dataconverge.dto.sbpz.SbConfigRequestDto;
import com.sailing.dataconverge.dto.sbzt.SbStateInfoDto;
import com.sailing.dataconverge.dto.wfsj.WfsjDto;

import java.util.List;

/**
 * 数据汇聚 Service，所有业务统一放一个 Service
 * create by en
 * at 2018/10/22 16:54
 **/
public interface ConvergeService {

    /**
     * 查询违法数据
     * @param sbdm          设备代码
     * @param startTime     开始时间
     * @param endTime       结束时间
     * @return
     */
    List<WfsjDto> queryWfsj(String sbdm, long startTime, long endTime);

    /**
     * 查询设备配置信息
     * @param sbdms      设备代码集合
     * @return
     */
    List<SbConfigInfoDto> querySbConfig(String sbdms);

    /**
     * 查询设备状态信息
     * @param sbdms      设备代码集合
     * @return
     */
    List<SbStateInfoDto> querySbState(String sbdms);



}
