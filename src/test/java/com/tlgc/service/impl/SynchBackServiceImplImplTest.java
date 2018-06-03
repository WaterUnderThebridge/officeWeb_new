package com.tlgc.service.impl;

import com.tlgc.entity.Intro;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class SynchBackServiceImplImplTest {
    @Autowired
    public SynchBackServiceImpl introService;
    @Test
    public void synchIntro() {
        Intro intro = new Intro();
        intro.setGymCode("980000");
        intro.setBabyName("davids");
        intro.setBabyBrithday("2018-03-24");
        intro.setChannel("");
        intro.setCreateTime(new Date());
        intro.setEmail("");
        intro.setStatus(1);
        intro.setParentPhone("13916488068");
        //log.info("intro:{}",intro);
        introService.synchIntro(intro);

    }
}