package com.tlgc.controller;


import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.tlgc.Convertor.DateConvert;
import com.tlgc.config.MyConfig;
import com.tlgc.entity.*;
import com.tlgc.enums.ResultEnum;
import com.tlgc.exception.MyException;
import com.tlgc.mapper.*;
import com.tlgc.utils.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Tony on 2017/8/31.
 */


@Slf4j
@RestController
@RequestMapping("/api")
public class InterfaceCtrl {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private ProvinceMapper provinceMapper;
    @Autowired
    private CityMapper cityMapper;
    @Autowired
    private GymMapper gymMapper;
    @Autowired
    private IntroMapper introMapper;
    @Autowired
    private MyConfig myConfig;

    @GetMapping(value = "/getPro")
    private JSONObject getProvince(HttpServletResponse rsp){
        rsp.addHeader("Access-Control-Allow-Origin", "*");
        rsp.setHeader("Content-Type", "application/json;charset=UTF-8");

        //  PageHelper.startPage(3,2);
        List<Province> p = provinceMapper.getAll();
        return DateConvert.toJson(ResultUtil.success(p));
    }
    @GetMapping(value = "/getCity/{provinceId}")
    private JSONObject getCity(@PathVariable("provinceId") Integer provinceId){
        //  PageHelper.startPage(3,2);
        List<City> cities = cityMapper.getAllByProvinceId(provinceId);
        return DateConvert.toJson(ResultUtil.success(cities));
    }

    @GetMapping(value = "/getGym/{cityId}")
    private JSONObject getGym(@PathVariable("cityId") Integer cityId){
        //  PageHelper.startPage(3,2);
        List<Gym> gyms = gymMapper.getAllByCityId(cityId);
        return DateConvert.toJson(ResultUtil.success(gyms));
    }
    @GetMapping(value = "/getGymByCity/{city}")
    private JSONObject getGymByCity(@PathVariable("city") String city){
        //  PageHelper.startPage(3,2);
        List<Gym> gyms = gymMapper.getAllByCity(city);
        return DateConvert.toJson(ResultUtil.success(gyms));
    }

    @RequestMapping(value = "/createIntro", method = RequestMethod.POST)
    public Result createAppoint(HttpServletResponse rsp, Intro intro){
        rsp.addHeader("Access-Control-Allow-Origin", "*");
        rsp.setHeader("Content-Type", "application/json;charset=UTF-8");
        intro.setMailStatus(0);
        intro.setChannel("php");
        intro.setCreateTime(new Date());
        intro.setSearch(intro.toString());
        if(introMapper.saveIntro(intro)>0) {
            return ResultUtil.success();
        }else {
            return ResultUtil.error();
        }
    }

    @RequestMapping(value = "/getIntro")
    public JSONObject getAppoint(HttpServletResponse rsp,
                                 @RequestParam(value = "pageNow",defaultValue = "1") Integer pageNow,
                                 @RequestParam(value = "size",defaultValue = "30") Integer size,
                                 @RequestParam(value = "keyWord",defaultValue = "") String keyWord,
                                 @RequestParam(value = "gymCode",defaultValue = "") String gymCode,
                                 @RequestParam(value = "dtBegin",defaultValue = "") String dtBegin,
                                 @RequestParam(value = "dtEnd",defaultValue = "") String dtEnd){
        PageHelper.startPage(pageNow,size);
        List<HashMap> Intros = introMapper.getIntro(gymCode,dtBegin,dtEnd,keyWord);
        return DateConvert.toJson(ResultUtil.success(Intros));
    }


}
