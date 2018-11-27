package com.sailing.dataconverge.convert;

import com.sailing.dataconverge.config.ConvergeConstent;
import com.sailing.dataconverge.domain.SbxxEntity;
import com.sailing.dataconverge.dto.sbpz.SbConfigInfoDto;
import com.sailing.dataconverge.dto.sbpz.SbConfigRequestDto;
import com.sailing.dataconverge.utils.CommUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * 设备信息转换器
 * create by en
 * at 2018/10/25 11:00
 **/
@Slf4j
public class SbxxEntityToDtoConvert {

    public static SbConfigInfoDto convert(SbxxEntity sbxx) {
        //  0-1. 设备信息判断：设备信息是否为空，预留字段2（即标准18位设备代码）是否为空，字段长度是否为18
        if (sbxx == null || CommUtils.isEmpty(sbxx.getYlzd2()) || sbxx.getYlzd2().length() != 18) {
            log.error("【设备配置信息】数据设备信息有问题，无设备信息或无标准18位设备代码，设备编号:{}", sbxx.getSbbh());
            return null;
        }
        SbConfigInfoDto sbConfigInfoDto = new SbConfigInfoDto();
        sbConfigInfoDto.setSbdm(sbxx.getYlzd2());
        sbConfigInfoDto.setIpdz(sbxx.getIpdz());
        sbConfigInfoDto.setPtdm(ConvergeConstent.PTDM());
        // 路口号
//        sbConfigInfoDto.setLkh(sbxx.getl);
        sbConfigInfoDto.setSbjd(sbxx.getJd());
        sbConfigInfoDto.setSbwd(sbxx.getWd());
        sbConfigInfoDto.setSbms(sbxx.getSbmc());
        // 摄像机信息 todo
        sbConfigInfoDto.setSxjpz(null);
        return sbConfigInfoDto;
    }

}
