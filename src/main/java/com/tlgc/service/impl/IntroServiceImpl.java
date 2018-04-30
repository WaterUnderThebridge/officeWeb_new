package com.tlgc.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sun.javafx.fxml.builder.URLBuilder;
import com.tlgc.Convertor.DataConvert;
import com.tlgc.config.IntroConfig;
import com.tlgc.entity.Intro;
import com.tlgc.service.IntroService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.net.URL;

@Slf4j
@Service
public class IntroServiceImpl implements IntroService {
    @Autowired
    IntroConfig introConfig;
    @Override

    public int synchIntro(Intro intro) {
        //log.info("intro:{}",DataConvert.makeParameters(intro));
        RestTemplate restTemplate = new RestTemplate();
        String param = DataConvert.makeParameters(intro);
        log.info(introConfig.getSynchUrl()+"?"+param);
        try {
            String result = restTemplate.getForObject(introConfig.getSynchUrl() + "?" + param, String.class);
            if (result == null) {
                return 0;
            }
            JSONObject jsonResult = JSONObject.parseObject(result);
        }catch(Exception e){
            e.printStackTrace();
            return 0;
        }
        return 1;
    }
}
