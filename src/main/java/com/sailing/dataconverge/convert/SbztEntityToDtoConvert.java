package com.sailing.dataconverge.convert;

import com.sailing.dataconverge.domain.SbxxEntity;
import com.sailing.dataconverge.domain.SbztEntity;
import com.sailing.dataconverge.dto.sbzt.SbStateInfoDto;

/**
 * 设备运维状态转换器
 * create by en
 * at 2018/10/25 11:12
 **/
public class SbztEntityToDtoConvert {

    public static SbStateInfoDto convert(SbztEntity sbzt) {
        SbStateInfoDto sbStateInfoDto = new SbStateInfoDto();
        sbStateInfoDto.setSbdm(sbzt.getSbbh());
//        sbStateInfoDto.setIpdz(sbxx.getIpdz());      // IP地址
        sbStateInfoDto.setTxzt("1".equals(sbzt.getSbztlx()) ? "y" : "n");
        sbStateInfoDto.setZt("1".equals(sbzt.getSbztlx()) ? "y" : "n");
//        sbStateInfoDto.setCpuzyl();           // cpu 占有率
//        sbStateInfoDto.setYpsyl(sbzt.get);    // 硬盘使用率
//        sbStateInfoDto.setSxjwzt(null);       // 摄像机运维状态
        return sbStateInfoDto;
    }

}
