package com.tlgc.controller;


import com.alibaba.fastjson.JSONObject;
import com.tlgc.Convertor.DateConvert;
import com.tlgc.config.MyConfig;
import com.tlgc.dao.CityDao;
import com.tlgc.entity.*;
import com.tlgc.mapper.*;
import com.tlgc.utils.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * Created by Tony on 2017/8/31.
 */


@Slf4j
@RestController
@RequestMapping("/interface")
public class testCtrl  {

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
    private JSONObject getProvince(){
        //  PageHelper.startPage(3,2);
        List<Province> p = provinceMapper.getAll();
        return DateConvert.toJson(ResultUtil.success(p));
    }
    @GetMapping(value = "/getCity/{provinceId}")
    private JSONObject getGym(@PathVariable("provinceId") Integer provinceId){
        //  PageHelper.startPage(3,2);
        List<City> cities = cityMapper.getAllByProvinceId(provinceId);
        return DateConvert.toJson(ResultUtil.success(cities));
    }

    @GetMapping(value = "/getGym/{cityId}")
    private JSONObject getCity(@PathVariable("cityId") Integer cityId){
        //  PageHelper.startPage(3,2);
        List<Gym> gyms = gymMapper.getAllByCityId(cityId);
        return DateConvert.toJson(ResultUtil.success(gyms));
    }

    @RequestMapping(value = "/createIntro", method = RequestMethod.POST)
    public Result createAppoint(HttpServletResponse rsp, Intro intro){
        rsp.addHeader("Access-Control-Allow-Origin", "*");
        rsp.setHeader("Content-Type", "application/json;charset=UTF-8");

        intro.setMailStatus(0);
        intro.setChannel("php");
        intro.setCreateTime(new Date());
        if(introMapper.saveIntro(intro)>0) {
            return ResultUtil.success();
        }else {
            return ResultUtil.error();
        }
    }



    @GetMapping(value = "/admin")
    private Admin who(){
        Admin admin= adminMapper.getAdminByUsername("market");
        return admin;
    }
    @GetMapping(value = "/conf")
    private String config(){
        return myConfig.toString();
    }


}