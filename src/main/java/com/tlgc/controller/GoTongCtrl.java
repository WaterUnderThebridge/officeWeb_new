package com.tlgc.controller;

import com.tlgc.Convertor.DataConvert;
import com.tlgc.dao.FranAppDao;
import com.tlgc.dao.GongTongDao;
import com.tlgc.entity.FranApp;
import com.tlgc.entity.GoTong;
import com.tlgc.enums.ResultEnum;
import com.tlgc.mapper.GotongMapper;
import com.tlgc.utils.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by TONY on 2019/6/27.
 */
@Slf4j
@RestController
@RequestMapping("/gt")
public class GoTongCtrl {
    @Autowired
    private GongTongDao gongTongDao;
    @Autowired
    private GotongMapper gotongMapper;
    @Autowired
    private FranAppDao franAppDao;
    @RequestMapping(value = "/save")
    public Object save(HttpServletRequest request, HttpServletResponse rsp,
                        @RequestParam(value = "content",defaultValue = "") String content,
                        @RequestParam(value = "dtGotong",defaultValue = "") Date dtGotong,
                        @RequestParam(value = "FranAppId",required = true) Integer FranAppId,
                        @RequestParam(value = "userId",required = true) Integer userId
    ){

        GoTong goTong=new GoTong();
        goTong.setContent(content);
        goTong.setDtGotong(dtGotong);
        goTong.setFranAppId(FranAppId);
        goTong.setUserId(userId);
        GoTong res=gongTongDao.save(goTong);
        if(res!=null)  return DataConvert.toJson(ResultUtil.success());
        return DataConvert.toJson(ResultUtil.error());
    }

    @RequestMapping(value = "/list")
    public Object list(HttpServletRequest request, HttpServletResponse rsp,
                       @RequestParam(value = "FranAppId",required = true) Integer FraAppId
    ){


         List<HashMap> goTongs=gotongMapper.getGotongs(FraAppId);
        if(goTongs!=null)  return DataConvert.toJson(ResultUtil.success(goTongs));
        return DataConvert.toJson(ResultUtil.error());
    }

    @RequestMapping(value = "/delete")
    public Object delete(HttpServletRequest request, HttpServletResponse rsp,
                       @RequestParam(value = "id",defaultValue = "") Integer id
    ){

        gongTongDao.delete(id);
        return DataConvert.toJson(ResultUtil.success());
    }
}
