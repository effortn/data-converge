package com.sailing.dataconverge.service;

import com.sailing.dataconverge.config.ConvergeConstent;
import com.sailing.dataconverge.dto.sbpz.SbConfigInfoDto;
import com.sailing.dataconverge.dto.wfsj.WfsjDto;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ConvergeServiceTest {

    @Autowired
    private ConvergeService convergeService;

    final static long startTime = System.currentTimeMillis() - 60 * 60 * 1000;

    final static long endTime = System.currentTimeMillis();

    final static String sbdms = "310113265020,310113235015,310113164028";

    @Test
    public void queryWfsj() {
        ConvergeConstent.initConstent();
//        List<WfsjDto> wfsjDtoList = convergeService.queryWfsj(sbdms, startTime, endTime);
        List<WfsjDto> wfsjDtoList = convergeService.queryWfsj(null, startTime, endTime);
        Assert.assertNotNull(wfsjDtoList);
        wfsjDtoList.forEach(wfsjDto -> {
            System.out.println(wfsjDto);
        });
    }

    @Test
    public void querySbConfig() {
        ConvergeConstent.initConstent();
        List<SbConfigInfoDto> sbConfigInfoDtoList = convergeService.querySbConfig(sbdms);
        Assert.assertNotNull(sbConfigInfoDtoList);
        sbConfigInfoDtoList.forEach(wfsjDto -> {
            System.out.println(wfsjDto);
        });
    }

    @Test
    public void querySbState() {


    }
}