package com.sailing.dataconverge.convert;

import com.sailing.dataconverge.config.ConvergeConfig;
import com.sailing.dataconverge.config.ConvergeConstent;
import lombok.Getter;

import java.io.File;

/**
 * 需要做代码转换的代码类型的枚举
 * create by en
 * at 2018/10/25 9:02
 **/
@Getter
public enum CodeConvertEnum {

    HPZL_CONVERT(ConvergeConstent.HPZL_CODE), //号牌种类的代码转换器
    HPYS_CONVERT(ConvergeConstent.HPYS_CODE), //号牌颜色的代码转换器
    CSYS_CONVERT(ConvergeConstent.CSYS_CODE), //车身颜色的代码转换器
    CLLX_CONVERT(ConvergeConstent.CLLX_CODE), //车辆类型的代码转换器
    CBLX_CONVERT(ConvergeConstent.CBLX_CODE), //车标类型的代码转换器
    WFLX_CONVERT(ConvergeConstent.WFLX_CODE), //违法类型的代码转换器
    DMFX_CONVERT(ConvergeConstent.DMFX_CODE), //断面方向的代码转换器
    ;

    CodeConvertEnum(String codeType) {
        this.codeConvert = new CodeConvert(codeType);
    }

    private CodeConvert codeConvert;

    public String getStandardCode(String systemCode) {
        return this.codeConvert.codeConvert(systemCode);
    }

}
