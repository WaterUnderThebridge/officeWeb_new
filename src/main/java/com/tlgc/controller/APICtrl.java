package com.tlgc.controller;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tlgc.Convertor.DataConvert;
import com.tlgc.entity.*;
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
    private NewsMapper newsMapper;
    @Autowired
    private FranAppMapper franAppMapper;

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
        return DataConvert.toJson(ResultUtil.success(cities),callback);
    }

    @GetMapping(value = "/getGym/{cityId}")
    private Object getGym(HttpServletResponse rsp,@RequestParam(value = "callback",required = false) String callback,@PathVariable("cityId") Integer cityId){
        rsp.addHeader("Access-Control-Allow-Origin", "*");
        rsp.setHeader("Content-Type", "application/json;charset=UTF-8");

        List<Gym> gyms = gymMapper.getAllByCityId(cityId);
        return DataConvert.toJson(ResultUtil.success(gyms),callback);
    }
    @GetMapping(value = "/getGymByCity/{city}")
    private Object getGymByCity(HttpServletResponse rsp,@RequestParam(value = "callback",required = false) String callback,@PathVariable("city") String city){
        rsp.addHeader("Access-Control-Allow-Origin", "*");
        rsp.setHeader("Content-Type", "application/json;charset=UTF-8");

        List<HashMap> gyms = gymMapper.getAllByCity(city);
        return DataConvert.toJson(ResultUtil.success(gyms),callback);
    }

    @RequestMapping(value = "/createIntro", method = RequestMethod.POST)
    public Object createAppoint(HttpServletResponse rsp,Intro intro){
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

    @RequestMapping(value = "/saveIntro")
    public Object createIntro(HttpServletResponse rsp,@RequestParam(value = "callback",required = false) String callback,
                              @RequestParam(value = "gymCode",defaultValue = "") String gymCode,
                              @RequestParam(value = "BabyName",defaultValue = "") String BabyName,
                              @RequestParam(value = "BabyBrithday",defaultValue = "") String BabyBrithday,
                              @RequestParam(value = "ParentPhone",defaultValue = "") String ParentPhone,
                              @RequestParam(value = "Province",defaultValue = "") String Province,
                              @RequestParam(value = "City",defaultValue = "") String City){
        rsp.addHeader("Access-Control-Allow-Origin", "*");
        rsp.setHeader("Content-Type", "application/json;charset=UTF-8");

        if(introMapper.findIntro(ParentPhone,gymCode)>0){
            return DataConvert.toJson(ResultUtil.error("该手机号在中心有预约体验"),callback);
        }
        Intro intro =new Intro();
        intro.setGymCode(gymCode);
        intro.setBabyBrithday(BabyBrithday);
        intro.setBabyName(BabyName);
        intro.setParentPhone(ParentPhone);
        intro.setProvince(Province);
        intro.setCity(City);
        intro.setMailStatus(0);
        intro.setStatus(1);
        intro.setChannel("test");
        intro.setCreateTime(new Date());
        intro.setSearch(intro.toString());
        if(introMapper.saveIntro(intro)>0) {
            return DataConvert.toJson(ResultUtil.success(),callback);
        }else {
            return DataConvert.toJson(ResultUtil.error(),callback);
        }
    }

    @RequestMapping(value = "/saveAppli")
    public Object saveAppli(HttpServletResponse rsp,@RequestParam(value = "callback",required = false) String callback,
                              @RequestParam(value = "UserName",defaultValue = "") String Name,
                              @RequestParam(value = "UserPhone",defaultValue = "") String Phone,
                              @RequestParam(value = "UserEmail",defaultValue = "") String Email,
                              @RequestParam(value = "City",defaultValue = "") String Address){
        rsp.addHeader("Access-Control-Allow-Origin", "*");
        rsp.setHeader("Content-Type", "application/json;charset=UTF-8");

        if(Phone.equals("")){
            return DataConvert.toJson(ResultUtil.error("手机号必须填写"),callback);
        }
        String dtApp =DataConvert.convert(new Date());
        log.info("res={}",franAppMapper.findApp(Phone,dtApp));
        if(franAppMapper.findApp(Phone,dtApp)>0){
            return DataConvert.toJson(ResultUtil.error("该手机号已提交申请，请耐心等待，我们会尽快处理"),callback);
        }
        FranApp franApp= new FranApp();
        franApp.setName(Name);
        franApp.setAddress(Address);
        franApp.setEmail(Email);
        franApp.setPhone(Phone);
        franApp.setMailStatus(0);
        franApp.setRemark("");
        franApp.setSearch(franApp.toString());
        if(franAppMapper.saveFranApp(franApp)>0) {
            return DataConvert.toJson(ResultUtil.success(),callback);
        }else {
            return DataConvert.toJson(ResultUtil.error(),callback);
        }
    }

    @GetMapping(value = "/listFranApp")
    private Object listFranApp(HttpServletResponse rsp,@RequestParam(value = "callback",required = false) String callback,
                               @RequestParam(value = "keyWord",defaultValue = "") String keyWord,
                               @RequestParam(value = "dtBegin",defaultValue = "") String dtBegin,
                               @RequestParam(value = "dtEnd",defaultValue = "") String dtEnd){
        rsp.addHeader("Access-Control-Allow-Origin", "*");
        rsp.setHeader("Content-Type", "application/json;charset=UTF-8");
        List<HashMap> apps = franAppMapper.listFranApp(dtBegin,dtEnd,keyWord);
        return DataConvert.toJson(ResultUtil.success(apps),callback);
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

    @RequestMapping(value = "/handleApp")
    public Object handleApp(HttpServletResponse rsp,@RequestParam(value = "callback",required = false) String callback,@RequestParam(value = "ids[]",required = true) String[] ids){
        rsp.addHeader("Access-Control-Allow-Origin", "*");
        rsp.setHeader("Content-Type", "application/json;charset=UTF-8");
        log.info(ids.toString());
        if(ids==null){
            return ResultUtil.error(-1,"applicants not selected");
        }
        if(franAppMapper.handleApp(ids)>0) {
            return DataConvert.toJson(ResultUtil.success(),callback);
        }else {
            return DataConvert.toJson(ResultUtil.error(),callback);
        }

    }




    @RequestMapping(value = "/getNews")
    public Object getNews(HttpServletResponse rsp,
                               @RequestParam(value = "callback",required = false) String callback,
                               @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
                               @RequestParam(value = "pageSize",defaultValue = "12") Integer size,
                               @RequestParam(value = "LanguageType",defaultValue = "1") Integer LanguageType,
                               @RequestParam(value = "type",required = false) Integer type){
        rsp.addHeader("Access-Control-Allow-Origin", "*");
        rsp.setHeader("Content-Type", "application/json;charset=UTF-8");
        log.info("{}",pageNum);
        PageHelper.startPage(pageNum,size);
        List<News> newsList = newsMapper.getNews(type,LanguageType);
        PageInfo<News> info = new PageInfo<>(newsList);
        return DataConvert.toJson(ResultUtil.success(info),callback);
    }

}
