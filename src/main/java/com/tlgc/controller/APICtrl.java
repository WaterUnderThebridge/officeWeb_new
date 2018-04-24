package com.tlgc.controller;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tlgc.Convertor.DataConvert;
import com.tlgc.entity.*;
import com.tlgc.mapper.*;
import com.tlgc.utils.ResultUtil;
import com.tlgc.utils.XmlUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private Object getProvince(HttpServletResponse rsp, @RequestParam(value = "callback", required = false) String callback) {
        rsp.addHeader("Access-Control-Allow-Origin", "*");
        rsp.setHeader("Content-Type", "application/json;charset=UTF-8");

        //  PageHelper.startPage(3,2);
        List<Province> p = provinceMapper.getAll();
        return DataConvert.toJson(ResultUtil.success(p), callback);
    }

    @GetMapping(value = "/getCity/{provinceId}")
    private Object getCity(HttpServletResponse rsp, @RequestParam(value = "callback", required = false) String callback, @PathVariable("provinceId") Integer provinceId) {
        rsp.addHeader("Access-Control-Allow-Origin", "*");
        rsp.setHeader("Content-Type", "application/json;charset=UTF-8");

        List<City> cities = cityMapper.getAllByProvinceId(provinceId);
        return DataConvert.toJson(ResultUtil.success(cities), callback);
    }

    @GetMapping(value = "/getGym/{cityId}")
    private Object getGym(HttpServletResponse rsp, @RequestParam(value = "callback", required = false) String callback, @PathVariable("cityId") Integer cityId) {
        rsp.addHeader("Access-Control-Allow-Origin", "*");
        rsp.setHeader("Content-Type", "application/json;charset=UTF-8");

        List<Gym> gyms = gymMapper.getAllByCityId(cityId);
        return DataConvert.toJson(ResultUtil.success(gyms), callback);
    }

    @GetMapping(value = "/getGymByCity/{city}")
    private Object getGymByCity(HttpServletResponse rsp, @RequestParam(value = "callback", required = false) String callback, @PathVariable("city") String city) {
        rsp.addHeader("Access-Control-Allow-Origin", "*");
        rsp.setHeader("Content-Type", "application/json;charset=UTF-8");

        List<HashMap> gyms = gymMapper.getAllByCity(city);
        return DataConvert.toJson(ResultUtil.success(gyms), callback);
    }

    @RequestMapping(value = "/createIntro", method = RequestMethod.POST)
    public Object createAppoint(HttpServletResponse rsp, Intro intro) {
        rsp.addHeader("Access-Control-Allow-Origin", "*");
        rsp.setHeader("Content-Type", "application/json;charset=UTF-8");
        intro.setMailStatus(0);
        intro.setChannel("php");
        intro.setCreateTime(new Date());
        intro.setSearch(intro.toString());
        if (introMapper.saveIntro(intro) > 0) {
            return DataConvert.toJson(ResultUtil.success());
        } else {
            return DataConvert.toJson(ResultUtil.error());
        }
    }

    @RequestMapping(value = "/saveIntro")
    public Object createIntro(HttpServletResponse rsp, @RequestParam(value = "callback", required = false) String callback,
                              @RequestParam(value = "gymCode", defaultValue = "") String gymCode,
                              @RequestParam(value = "BabyName", defaultValue = "") String BabyName,
                              @RequestParam(value = "BabyBrithday", defaultValue = "") String BabyBrithday,
                              @RequestParam(value = "ParentPhone", defaultValue = "") String ParentPhone,
                              @RequestParam(value = "Province", defaultValue = "") String Province,
                              @RequestParam(value = "City", defaultValue = "") String City) {
        rsp.addHeader("Access-Control-Allow-Origin", "*");
        rsp.setHeader("Content-Type", "application/json;charset=UTF-8");

        if (introMapper.findIntro(ParentPhone, gymCode) > 0) {
            return DataConvert.toJson(ResultUtil.error("该手机号在中心有预约体验"), callback);
        }
        Intro intro = new Intro();
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
        if (introMapper.saveIntro(intro) > 0) {
            return DataConvert.toJson(ResultUtil.success(), callback);
        } else {
            return DataConvert.toJson(ResultUtil.error(), callback);
        }
    }

    @RequestMapping(value = "/saveAppli")
    public Object saveAppli(HttpServletResponse rsp, @RequestParam(value = "callback", required = false) String callback,
                            @RequestParam(value = "Name", defaultValue = "") String Name,
                            @RequestParam(value = "Phone", defaultValue = "") String Phone,
                            @RequestParam(value = "Email", defaultValue = "") String Email,
                            @RequestParam(value = "Address", defaultValue = "") String Address) {
        rsp.addHeader("Access-Control-Allow-Origin", "*");
        rsp.setHeader("Content-Type", "application/json;charset=UTF-8");

        if (Phone.equals("")) {
            return DataConvert.toJson(ResultUtil.error("手机号必须填写"), callback);
        }
        if (franAppMapper.findApp(Phone) > 0) {
            return DataConvert.toJson(ResultUtil.error("该手机号已提交申请，我们会尽快处理"), callback);
        }
        FranApp franApp = new FranApp();
        franApp.setName(Name);
        franApp.setAddress(Address);
        franApp.setEmail(Email);
        franApp.setMailStatus(0);
        franApp.setSearch(franApp.toString());
        if (franAppMapper.saveFranApp(franApp) > 0) {
            return DataConvert.toJson(ResultUtil.success(), callback);
        } else {
            return DataConvert.toJson(ResultUtil.error(), callback);
        }
    }

    @GetMapping(value = "/listFranApp")
    private Object listFranApp(HttpServletResponse rsp, @RequestParam(value = "callback", required = false) String callback,
                               @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNow,
                               @RequestParam(value = "pageSize", defaultValue = "30") Integer size,
                               @RequestParam(value = "keyWord", defaultValue = "") String keyWord,
                               @RequestParam(value = "status", required = false) String status,
                               @RequestParam(value = "dtBegin", defaultValue = "") String dtBegin,
                               @RequestParam(value = "dtEnd", defaultValue = "") String dtEnd) {
        rsp.addHeader("Access-Control-Allow-Origin", "*");
        rsp.setHeader("Content-Type", "application/json;charset=UTF-8");
        PageHelper.startPage(pageNow, size);
        List<HashMap> apps = franAppMapper.listFranApp(dtBegin, dtEnd, keyWord, status);
        return DataConvert.toJson(ResultUtil.success(apps), callback);
    }

    @RequestMapping(value = "/getIntro")
    public Object getIntro(HttpServletResponse rsp,
                           @RequestParam(value = "callback", required = false) String callback,
                           @RequestParam(value = "roleId", defaultValue = "2") String roleId,
                           @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNow,
                           @RequestParam(value = "pageSize", defaultValue = "30") Integer size,
                           @RequestParam(value = "keyWord", defaultValue = "") String keyWord,
                           @RequestParam(value = "gymCode", required = false) String gymCode,
                           @RequestParam(value = "dtBegin", defaultValue = "") String dtBegin,
                           @RequestParam(value = "dtEnd", defaultValue = "") String dtEnd) {
        rsp.addHeader("Access-Control-Allow-Origin", "*");
        rsp.setHeader("Content-Type", "application/json;charset=UTF-8");

        if (roleId.equals("2") && gymCode == null) {
            return DataConvert.toJson(ResultUtil.error(-1, "中心名称不能为空"), callback);
        }
        PageHelper.startPage(pageNow, size);
        List<HashMap> Intros = introMapper.getIntro(gymCode, dtBegin, dtEnd, keyWord);
        PageInfo<HashMap> info = new PageInfo<>(Intros);
        return DataConvert.toJson(ResultUtil.success(info), callback);
    }

    @RequestMapping(value = "/handleIntro")
    public Object handleIntro(HttpServletResponse rsp, @RequestParam(value = "callback", required = false) String callback, @RequestParam(value = "ids[]", required = true) String[] ids) {
        rsp.addHeader("Access-Control-Allow-Origin", "*");
        rsp.setHeader("Content-Type", "application/json;charset=UTF-8");
        log.info(ids.toString());
        if (ids == null) {
            return ResultUtil.error(-1, "Intro not selected");
        }

        if (introMapper.handleIntro(ids) > 0) {
            return DataConvert.toJson(ResultUtil.success(), callback);
        } else {
            return DataConvert.toJson(ResultUtil.error(), callback);
        }

    }


    @RequestMapping(value = "/getNews")
    public Object getNews(HttpServletResponse rsp,
                          @RequestParam(value = "callback", required = false) String callback,
                          @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                          @RequestParam(value = "pageSize", defaultValue = "12") Integer size,
                          @RequestParam(value = "LanguageType", defaultValue = "1") Integer LanguageType,
                          @RequestParam(value = "type", required = false) Integer type) {
        rsp.addHeader("Access-Control-Allow-Origin", "*");
        rsp.setHeader("Content-Type", "application/json;charset=UTF-8");
        log.info("{}", pageNum);
        PageHelper.startPage(pageNum, size);
        List<News> newsList = newsMapper.getNews(type, LanguageType);
        PageInfo<News> info = new PageInfo<>(newsList);
        return DataConvert.toJson(ResultUtil.success(info), callback);
    }

    @RequestMapping(value = "/syncGym")
    public ModelAndView syncGym(@RequestBody String param, Map<String, Object> res, HttpServletResponse rsp) {
        rsp.addHeader("Access-Control-Allow-Origin", "*");
        rsp.setHeader("Content-Type", "text/xml;charset=UTF-8");
        //String soap="<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><soap:Body><mGetOutMessage xmlns=\"http://tempuri.org/crm_server/Crm_OutMessage\"><outobject><UserID>279833</UserID><ID>4861</ID><SessionID /><ObjectName>crm_zdytable_238592_26277</ObjectName><fields><sobjectField><fieldsign>手机号</fieldsign><fieldname>crmzdy_81762775</fieldname><fieldvalue>test</fieldvalue><fieldtype>文本</fieldtype></sobjectField><sobjectField><fieldsign>短信内容</fieldsign><fieldname>crmzdy_81762774</fieldname><fieldvalue>stes</fieldvalue><fieldtype>文本</fieldtype></sobjectField><sobjectField><fieldsign>中心编号</fieldsign><fieldname>crmzdy_81762776</fieldname><fieldvalue>500012</fieldvalue><fieldtype>文本</fieldtype></sobjectField><sobjectField><fieldsign>记录ID</fieldsign><fieldname>id</fieldname><fieldvalue>4861</fieldvalue><fieldtype>整数</fieldtype></sobjectField></fields></outobject></mGetOutMessage></soap:Body></soap:Envelope>";
        //初始化报文，调用parse方法，获得结果map
        try {
            Map map = new XmlUtil().parse(param);
            res.put("id", map.get("ID").toString());
            //System.out.println(map);

        } catch (Exception e) {
            res.put("isSuccess", "false");
            res.put("error", "未知错误");
            e.printStackTrace();
        }
        res.put("isSuccess","true");

        return new ModelAndView("message/success", res);

    }
}
