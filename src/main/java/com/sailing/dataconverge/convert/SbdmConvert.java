package com.sailing.dataconverge.convert;

import com.sailing.dataconverge.domain.SbxxEntity;
import com.sailing.dataconverge.repository.SbxxRepository;
import com.sailing.dataconverge.utils.CommUtils;
import com.sailing.dataconverge.utils.SpringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 对中心请求的设备编号进行转换，转换为本地设备编号
 * create by en
 * at 2018/10/26 13:01
 **/
public class SbdmConvert {

    // 18位的设备编号对应12位的设备编号的集合
    private static Map<String, String> SBDM_MAP;

    /**
     * 集合初始化
     */
    private synchronized static void init() {
        SbxxRepository sbxxRepository = (SbxxRepository) SpringUtils.getBean(SbxxRepository.class);
        List<SbxxEntity> sbxxEntityList = sbxxRepository.findAll();
        Map<String, String> tmpMap = new HashMap<>();
        sbxxEntityList.forEach(sbxxEntity -> {
            if (!CommUtils.isEmpty(sbxxEntity.getYlzd2()) && sbxxEntity.getYlzd2().length() == 18)
                tmpMap.put(sbxxEntity.getYlzd2(), sbxxEntity.getSbbh());
        });
        SBDM_MAP = tmpMap;
    }

    /**
     * 通过中心设备代码获取本地设备代码
     * @param zxsbdm
     * @return
     */
    public static String getBdsbdm(String zxsbdm) {
        if (SBDM_MAP == null) {
            synchronized (SbdmConvert.class) {
                if (SBDM_MAP == null) {
                    init();
                }
            }
        }
        return SBDM_MAP.get(zxsbdm);
    }

    public static String getBdsbdms(String zxsbdms) {
        if (SBDM_MAP == null) {
            synchronized (SbdmConvert.class) {
                if (SBDM_MAP == null) {
                    init();
                }
            }
        }
        String[] zxsbdmArray = zxsbdms.split(",");
        String bdsbdms = "";
        for (int i = 0; i < zxsbdmArray.length; i++) {
            String bdsbdm = SBDM_MAP.get(zxsbdmArray[i]);
            if (!CommUtils.isEmpty(bdsbdm))
                bdsbdms = bdsbdms.concat(bdsbdm);
            if (i < zxsbdmArray.length - 1)
                bdsbdms = bdsbdms.concat(",");
        }
        return bdsbdms;
    }

    /**
     * 重新加载设备信息
     */
    public static void initSbxx() {
        init();
    }

}
