package com.tlgc.controller.API;


import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPObject;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.tlgc.Convertor.DataConvert;
import com.tlgc.config.MyConfig;
import com.tlgc.entity.*;
import com.tlgc.mapper.*;
import com.tlgc.service.WebSocket;
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
    @Autowired
    private WebSocket webSocket;

    @GetMapping(value = "/getPro")
    private JSONObject getProvince(@RequestParam(value="callback",required = false)String callback){
        //  PageHelper.startPage(3,2);
        List<Province> p = provinceMapper.getAll();
        return DataConvert.toJson(ResultUtil.success(p));
    }
    @GetMapping(value = "/test")
    private void socket(HttpServletResponse rsp,@RequestParam(value="text")String text,@RequestParam(value="callback",required = false)String callback){
        webSocket.sendMessage(text);
        try {
            rsp.getWriter().println(text);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @GetMapping(value = "/test2")
    private String getProvince2(HttpServletResponse rsp,@RequestParam(value="callback",required = false)String callback){
        rsp.addHeader("Access-Control-Allow-Origin", "*");
        rsp.setHeader("Content-Type", "application/json;charset=UTF-8");

        List<Province> p = provinceMapper.getAll();
        return DataConvert.toJsonStr(ResultUtil.success(p),callback);
    }
    @GetMapping(value = "/getCity/{provinceId}")
    private JSONObject getGym(@PathVariable("provinceId") Integer provinceId){
        //  PageHelper.startPage(3,2);
        List<City> cities = cityMapper.getAllByProvinceId(provinceId);
        return DataConvert.toJson(ResultUtil.success(cities));
    }

    @GetMapping(value = "/getGym/{cityId}")
    private JSONObject getCity(@PathVariable("cityId") Integer cityId){
        //  PageHelper.startPage(3,2);
        List<HashMap> gyms = gymMapper.getAllByCityId(cityId,"0");
        return DataConvert.toJson(ResultUtil.success(gyms));
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

    @RequestMapping(value = "/syncGym")
    public String syncGym(Map<String, Object> res, HttpServletResponse rsp) {
        rsp.setHeader("Content-Type", "text/xml;charset=UTF-8");
        rsp.addHeader("Access-Control-Allow-Origin", "*");
        String soap="<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><soap:Body><mGetOutMessage xmlns=\"http://tempuri.org/crm_server/Crm_OutMessage\"><outobject><UserID>279833</UserID><ID>4861</ID><SessionID /><ObjectName>crm_zdytable_238592_26277</ObjectName><fields><sobjectField><fieldsign>手机号</fieldsign><fieldname>crmzdy_81762775</fieldname><fieldvalue>test</fieldvalue><fieldtype>文本</fieldtype></sobjectField><sobjectField><fieldsign>短信内容</fieldsign><fieldname>crmzdy_81762774</fieldname><fieldvalue>stes</fieldvalue><fieldtype>文本</fieldtype></sobjectField><sobjectField><fieldsign>中心编号</fieldsign><fieldname>crmzdy_81762776</fieldname><fieldvalue>500012</fieldvalue><fieldtype>文本</fieldtype></sobjectField><sobjectField><fieldsign>记录ID</fieldsign><fieldname>id</fieldname><fieldvalue>4861</fieldvalue><fieldtype>整数</fieldtype></sobjectField></fields></outobject></mGetOutMessage></soap:Body></soap:Envelope>";
        //初始化报文，调用parse方法，获得结果map

        XmlUtil xml = new XmlUtil(soap);
        log.info("{}",xml.getRes());
//        if(!xml.isSucess){
//
//            res.put("isSuccess", "false");
//            res.put("error", "未知错误");
//        }

        return "sss";

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
