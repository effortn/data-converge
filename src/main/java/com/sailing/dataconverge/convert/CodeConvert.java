package com.sailing.dataconverge.convert;

import com.sailing.dataconverge.config.ConvergeConfig;
import com.sailing.dataconverge.utils.CommUtils;
import com.sailing.dataconverge.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 平台代码转换
 * create by en
 *
 * at 2018/10/24 18:03
 **/
@Slf4j
public class CodeConvert {

    private Map<String, String> codeConvertMap;

    private final static String DEFAULT_KEY = "default";

    public CodeConvert(String codeType) {
        ConvergeConfig convergeConfig = (ConvergeConfig) SpringUtils.getBean(ConvergeConfig.class);
        Map<String, String> codeConvertFileMap = convergeConfig.getCodeConvertMap();
        if (codeConvertFileMap == null || !codeConvertFileMap.containsKey(codeType)) {
            this.codeConvertMap = null;
            return;
        }
        String filePath = codeConvertFileMap.get(codeType);
        if (CommUtils.isEmpty(filePath)) {
            this.codeConvertMap = null;
            return;
        }
        File codeConvertFile = new File(filePath);
        if (codeConvertFile ==null || !codeConvertFile.exists()) {
            log.error("【代码转换】文件不存在！file:{}", codeConvertFile.getPath());
            this.codeConvertMap = null;
            return;
        }
        this.codeConvertMap = new HashMap<>();
        try {
            log.info("【代码转换】初始化，file:{}",
                    codeConvertFile.getPath());
            BufferedReader br = new BufferedReader(new FileReader(codeConvertFile));
            String line;
            while ((line = br.readLine()) != null) {
                String[] codePair = line.split("-");
                this.codeConvertMap.put(codePair[0], codePair[1]);
            }
            br.close();
            log.info("【代码转换】初始化完成，file:{}",
                    codeConvertFile.getPath());
        } catch (Exception e) {
            this.codeConvertMap = null;
            e.printStackTrace();
            log.error("【代码转换】初始化报错，file:{}.",
                    codeConvertFile.getPath(), e);
        }
    }

    public CodeConvert(File codeConvertFile) {
        if (codeConvertFile ==null || !codeConvertFile.exists()) {
            log.error("【代码转换】文件不存在！file:{}", codeConvertFile.getPath());
            this.codeConvertMap = null;
            return;
        }
        try {
            log.info("【代码转换】初始化，file:{}",
                    codeConvertFile.getPath() + codeConvertFile.getName());
            BufferedReader br = new BufferedReader(new FileReader(codeConvertFile));
            String line;
            while ((line = br.readLine()) != null) {
                String[] codePair = line.split("-");
                codeConvertMap.put(codePair[0], codePair[1]);
            }
            br.close();
            log.info("【代码转换】初始化完成，file:{}",
                    codeConvertFile.getPath() + codeConvertFile.getName());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("【代码转换】初始化报错，file:{}.",
                    codeConvertFile.getPath() + codeConvertFile.getName(), e);
            this.codeConvertMap = null;
        }
    }

    /**
     * 代码转换
     * @param ptdm
     * @return
     */
    public String codeConvert(String ptdm) {
        if (codeConvertMap == null)
            return ptdm;
        if (codeConvertMap.containsKey(ptdm))
            return codeConvertMap.get(ptdm);
        return codeConvertMap.get(DEFAULT_KEY);
    }

}
