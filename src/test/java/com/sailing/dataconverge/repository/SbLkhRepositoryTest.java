package com.sailing.dataconverge.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class SbLkhRepositoryTest {

    @Autowired
    private SbLkhRepository sbLkhRepository;

    @Test
    public void querySysdate() {
        String s = sbLkhRepository.querySysdate();
        System.out.printf(s);
    }
}