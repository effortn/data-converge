package com.sailing.dataconverge.convert;

import com.sailing.dataconverge.config.ConvergeConstent;
import com.sailing.dataconverge.domain.WftcEntity;
import com.sailing.dataconverge.dto.wfsj.WfsjDto;
import lombok.extern.slf4j.Slf4j;

/**
 * 违法数据表数据转换为 dto
 * create by en
 * at 2018/10/24 17:58
 **/
@Slf4j
public class WftcEntityToDtoConvert {

    public static WfsjDto convert(WftcEntity wf) {
        log.debug("【违法数据转换】违法源数据:{}", wf);
        WfsjDto wfsjDto = new WfsjDto();

        wfsjDto.setHphm(wf.getHphm());
//        wfsjDto.setHpzl();
        wfsjDto.setHpzl(CodeConvertEnum.HPZL_CONVERT.getStandardCode(String.valueOf(wf.getHpzl())));
        wfsjDto.setHpys(CodeConvertEnum.HPYS_CONVERT.getStandardCode(String.valueOf(wf.getHpys())));
        try {
            wfsjDto.setClsd(wf.getHphm().substring(0, 2));
        } catch (Exception e) {
            e.printStackTrace();
            log.error("【违法数据转换】车辆属地转换报错！", e);
            wfsjDto.setClsd("999");
        }
        wfsjDto.setJgsj(ConvergeConstent.DATE_TIME_FORMATTER_JGSJ.print(wf.getJgsj().getTime()));
        wfsjDto.setCsys(CodeConvertEnum.CSYS_CONVERT.getStandardCode(String.valueOf(wf.getCsys())));
        /**
         * 车标类型暂无
         * wfsjDto.setCblx(CodeConvertEnum.CBLX_CONVERT.getStandardCode(wf.getC));
         */
        //  图片ID 在service 中进行处理
//        wfsjDto.setTpid();
        wfsjDto.setCs((int) Math.round(wf.getClsd()));
        wfsjDto.setPtdm(ConvergeConstent.PTDM());
        wfsjDto.setSbdm(wf.getSbbh());
        /**
         * // 路口号在 service 中设置
         * wfsjDto.setLkh(wf.get);
         */
        wfsjDto.setDmfx(Integer.valueOf(CodeConvertEnum.DMFX_CONVERT.getStandardCode(wf.getFxbh())));
        // todo 断面描述
//        wfsjDto.setDmms(wf.get);
        wfsjDto.setCdbh(String.valueOf(wf.getCdbh()));
        wfsjDto.setCscd((int) Math.round(wf.getCwkc()));
        wfsjDto.setCllx(CodeConvertEnum.CLLX_CONVERT.getStandardCode(wf.getCllx()));
//        wfsjDto.setCx(wf.getC);   // 车型
//        wfsjDto.setCcm(wf.getC);  // 出错码
//        wfsjDto.setCcyy();        // 出错原因
//        wfsjDto.setSbsj(wf.ge);   // 识别时间
//        wfsjDto.setHpjg();        // 号牌结构
        wfsjDto.setWflx(0);        //违法类型,卡口过车数据填0
        wfsjDto.setHdsj((int) Math.round(wf.getHdsj()));
        // 图片，录像 在service 中进行处理
//        wfsjDto.setTpsl((int) Math.ceil(wf.getTpsl()));
//        wfsjDto.setTpdx();
//        wfsjDto.setTpdx();
//        wfsjDto.setTpqdz();
//        wfsjDto.setTpzxdz();
//        wfsjDto.setLxsl();
//        wfsjDto.setLxwjlx();
//        wfsjDto.setLxqdx();
//        wfsjDto.setLxzxdz();
        return wfsjDto;
    }

}
