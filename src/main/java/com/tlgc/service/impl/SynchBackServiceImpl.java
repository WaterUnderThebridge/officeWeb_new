package com.tlgc.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.tlgc.Convertor.DataConvert;
import com.tlgc.config.SyncUrlConfig;
import com.tlgc.entity.Intro;
import com.tlgc.service.ISynchBackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class SynchBackServiceImpl implements ISynchBackService {
    @Autowired
    SyncUrlConfig syncUrlConfig;
    @Override
    public int synchIntro(Intro intro) {
        //log.info("intro:{}",DataConvert.makeParameters(intro));
        RestTemplate restTemplate = new RestTemplate();
        String param = DataConvert.makeParameters(intro);
        log.info(syncUrlConfig.getIntroUrl()+"?"+param);
        try {
            String result = restTemplate.getForObject(syncUrlConfig.getIntroUrl() + "?" + param, String.class);
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

    @Override
    public int synchGt(String id) {
        RestTemplate restTemplate = new RestTemplate();
        log.info(syncUrlConfig.getGtUrl() + "?mid=" + id);
        try {
            String result = restTemplate.getForObject(syncUrlConfig.getGtUrl() + "?mid=" + id, String.class);
            if (result == null) {
                return 0;
            }
        }catch(Exception e){
            e.printStackTrace();
            return 0;
        }
        return 1;
    }
}
