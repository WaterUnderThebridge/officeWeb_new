package com.tlgc.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.JSONSerializable;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tlgc.Convertor.DataConvert;
import com.tlgc.config.MyConfig;
import com.tlgc.entity.*;
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
public class APICtrl {

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
    private Object getProvince(HttpServletResponse rsp,@RequestParam(value = "callback",required = false) String callback){
        rsp.addHeader("Access-Control-Allow-Origin", "*");
        rsp.setHeader("Content-Type", "application/json;charset=UTF-8");

        //  PageHelper.startPage(3,2);
        List<Province> p = provinceMapper.getAll();
        return DataConvert.toJson(ResultUtil.success(p),callback);
    }
    @GetMapping(value = "/getCity/{provinceId}")
    private Object getCity(HttpServletResponse rsp,@RequestParam(value = "callback",required = false) String callback,@PathVariable("provinceId") Integer provinceId){
        rsp.addHeader("Access-Control-Allow-Origin", "*");
        rsp.setHeader("Content-Type", "application/json;charset=UTF-8");

        List<City> cities = cityMapper.getAllByProvinceId(provinceId);
        return DataConvert.toJson(ResultUtil.success(cities));
    }

    @GetMapping(value = "/getGym/{cityId}")
    private Object getGym(HttpServletResponse rsp,@RequestParam(value = "callback",required = false) String callback,@PathVariable("cityId") Integer cityId){
        rsp.addHeader("Access-Control-Allow-Origin", "*");
        rsp.setHeader("Content-Type", "application/json;charset=UTF-8");

        List<Gym> gyms = gymMapper.getAllByCityId(cityId);
        return DataConvert.toJson(ResultUtil.success(gyms));
    }
    @GetMapping(value = "/getGymByCity/{city}")
    private Object getGymByCity(HttpServletResponse rsp,@RequestParam(value = "callback",required = false) String callback,@PathVariable("city") String city){
        rsp.addHeader("Access-Control-Allow-Origin", "*");
        rsp.setHeader("Content-Type", "application/json;charset=UTF-8");

        List<Gym> gyms = gymMapper.getAllByCity(city);
        return DataConvert.toJson(ResultUtil.success(gyms));
    }

    @RequestMapping(value = "/createIntro", method = RequestMethod.POST)
    public Object createAppoint(HttpServletResponse rsp,@RequestParam(value = "callback",required = false) String callback, Intro intro){
        rsp.addHeader("Access-Control-Allow-Origin", "*");
        rsp.setHeader("Content-Type", "application/json;charset=UTF-8");
        intro.setMailStatus(0);
        intro.setChannel("php");
        intro.setCreateTime(new Date());
        intro.setSearch(intro.toString());
        if(introMapper.saveIntro(intro)>0) {
            return DataConvert.toJson(ResultUtil.success());
        }else {
            return DataConvert.toJson(ResultUtil.error());
        }
    }

    @RequestMapping(value = "/getIntro")
    public Object getIntro(HttpServletResponse rsp,
                                 @RequestParam(value = "callback",required = false) String callback,
                                 @RequestParam(value = "roleId",defaultValue ="2") String roleId,
                                 @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNow,
                                 @RequestParam(value = "pageSize",defaultValue = "30") Integer size,
                                 @RequestParam(value = "keyWord",defaultValue = "") String keyWord,
                                 @RequestParam(value = "gymCode",required = false) String gymCode,
                                 @RequestParam(value = "dtBegin",defaultValue = "") String dtBegin,
                                 @RequestParam(value = "dtEnd",defaultValue = "") String dtEnd){
        rsp.addHeader("Access-Control-Allow-Origin", "*");
        rsp.setHeader("Content-Type", "application/json;charset=UTF-8");

        if(roleId.equals("2") && gymCode==null){
            return DataConvert.toJson(ResultUtil.error(-1,"中心名称不能为空"),callback);
        }
        PageHelper.startPage(pageNow,size);
        List<HashMap> Intros = introMapper.getIntro(gymCode,dtBegin,dtEnd,keyWord);
        PageInfo<HashMap> info = new PageInfo<>(Intros);
        return DataConvert.toJson(ResultUtil.success(info),callback);
    }

    @RequestMapping(value = "/handleIntro")
    public Object handleIntro(HttpServletResponse rsp,@RequestParam(value = "callback",required = false) String callback,@RequestParam(value = "ids[]",required = true) String[] ids){
        rsp.addHeader("Access-Control-Allow-Origin", "*");
        rsp.setHeader("Content-Type", "application/json;charset=UTF-8");
        log.info(ids.toString());
        if(ids==null){
            return ResultUtil.error(-1,"Intro not selected");
        }

        if(introMapper.handleIntro(ids)>0) {
            return DataConvert.toJson(ResultUtil.success(),callback);
        }else {
            return DataConvert.toJson(ResultUtil.error(),callback);
        }

    }

}
