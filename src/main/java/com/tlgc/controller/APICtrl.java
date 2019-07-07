package com.tlgc.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tlgc.Convertor.DataConvert;
import com.tlgc.entity.*;
import com.tlgc.mapper.*;
import com.tlgc.service.ISmsService;
import com.tlgc.service.ISynchBackService;
import com.tlgc.service.impl.SmsServiceImpl;
import com.tlgc.utils.ResultUtil;
import com.tlgc.utils.XmlUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

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
    private FranAppMapper franAppMapper;
    @Autowired
    private ISynchBackService iSynchBackService;
    @Autowired
    private ISmsService iSmsService;

    @GetMapping(value = "/getPro")
    private Object getProvince(HttpServletResponse rsp, @RequestParam(value = "callback", required = false) String callback) {

        //  PageHelper.startPage(3,2);
        List<Province> p = provinceMapper.getAll();
        return DataConvert.toJson(ResultUtil.success(p), callback);
    }

    @GetMapping(value = "/getCity/{provinceId}")
    private Object getCity(HttpServletResponse rsp, @RequestParam(value = "callback", required = false) String callback, @PathVariable("provinceId") Integer provinceId) {
//        rsp.addHeader("Access-Control-Allow-Origin", "*");
//        rsp.setHeader("Content-Type", "application/json;charset=UTF-8");

        List<City> cities = cityMapper.getAllByProvinceId(provinceId);
        return DataConvert.toJson(ResultUtil.success(cities), callback);
    }

    @GetMapping(value = "/getGym/{cityId}")
    private Object getGym(HttpServletResponse rsp, @RequestParam(value = "callback", required = false) String callback, @PathVariable("cityId") Integer cityId) {
//        rsp.addHeader("Access-Control-Allow-Origin", "*");
//        rsp.setHeader("Content-Type", "application/json;charset=UTF-8");

        List<Gym> gyms = gymMapper.getAllByCityId(cityId);
        return DataConvert.toJson(ResultUtil.success(gyms), callback);
    }

    @GetMapping(value = "/getGymByCity/{city}")
    private Object getGymByCity(HttpServletResponse rsp, @RequestParam(value = "callback", required = false) String callback, @PathVariable("city") String city) {
//        rsp.addHeader("Access-Control-Allow-Origin", "*");
//        rsp.setHeader("Content-Type", "application/json;charset=UTF-8");

        List<HashMap> gyms = gymMapper.getAllByCity(city);
        return DataConvert.toJson(ResultUtil.success(gyms), callback);
    }

    @RequestMapping(value = "/createIntro", method = RequestMethod.POST)
    public Object createAppoint(HttpServletResponse rsp, Intro intro) {
//        rsp.addHeader("Access-Control-Allow-Origin", "*");
//        rsp.setHeader("Content-Type", "application/json;charset=UTF-8");
        intro.setMailStatus(0);
        intro.setGymCode(intro.getCenter());
        intro.setChannel("手机页面登记（中心市场活动）");
        intro.setStatus(1);
        intro.setIsSync(0);
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
                              @RequestParam(value = "channel", defaultValue = "") String channel,
                              @RequestParam(value = "City", defaultValue = "") String City) {
//        rsp.addHeader("Access-Control-Allow-Origin", "*");
//        rsp.setHeader("Content-Type", "application/json;charset=UTF-8");

        if (introMapper.findIntro(ParentPhone, gymCode) > 0) {
            return DataConvert.toJson(ResultUtil.error("该手机号在中心有预约体验"), callback);
        }
        Intro intro = new Intro();
        intro.setGymCode(gymCode);
        intro.setCenter(gymCode);
        intro.setBabyBrithday(BabyBrithday);
        intro.setBabyName(BabyName);
        intro.setParentPhone(ParentPhone);
        intro.setProvince(Province);
        intro.setCity(City);
        intro.setMailStatus(0);
        intro.setStatus(1);
        intro.setChannel(channel);
        intro.setCreateTime(new Date());
        intro.setSearch(intro.toString());
        intro.setIsSync(iSynchBackService.synchIntro(intro));
        if (introMapper.saveIntro(intro) > 0) {
            return DataConvert.toJson(ResultUtil.success(), callback);
        } else {
            return DataConvert.toJson(ResultUtil.error(), callback);
        }
    }

    @RequestMapping(value = "/saveAppli")
    public Object saveAppli(HttpServletResponse rsp,@RequestParam(value = "callback",required = false) String callback,
                              @RequestParam(value = "UserName",defaultValue = "") String Name,
                              @RequestParam(value = "UserPhone",defaultValue = "") String Phone,
                              @RequestParam(value = "wechatName",defaultValue = "") String wechatName,
                              @RequestParam(value = "UserEmail",defaultValue = "") String Email,
                              @RequestParam(value = "Time",defaultValue = "") Integer LinkTime,
                              @RequestParam(value = "Channel",defaultValue = "") String Channel,
                              @RequestParam(value = "rec_phone",defaultValue = "") String rec_phone,
                              @RequestParam(value = "rec_name",defaultValue = "") String rec_name,
                              @RequestParam(value = "City",defaultValue = "") String Address,
                              @RequestParam(value = "dt",defaultValue = "") String dt,
                              @RequestParam(value = "followerId",defaultValue = "0") Integer followerId
    ){
//        rsp.addHeader("Access-Control-Allow-Origin", "*");
//        rsp.setHeader("Content-Type", "application/json;charset=UTF-8");

        if (Phone.equals("")) {
            return DataConvert.toJson(ResultUtil.error("手机号必须填写"), callback);
        }
        String dtApp;
        if(!dt.equals("")){
            dtApp=dt;
        }else{
            dtApp =DataConvert.convert(new Date());
        }
        if(franAppMapper.findApp(Phone,dtApp)>0){
            return DataConvert.toJson(ResultUtil.error("该手机号已提交申请，请耐心等待，我们会尽快处理"),callback);

        }
        FranApp franApp = new FranApp();
        franApp.setName(Name);
        franApp.setAddress(Address);
        franApp.setChannel(Channel);
        franApp.setLinkTime(LinkTime);
        franApp.setWechatName(wechatName);
        franApp.setEmail(Email);
        franApp.setPhone(Phone);
        franApp.setMailStatus(0);
        franApp.setRemark("");
        franApp.setRec_name(rec_name);
        franApp.setRec_phone(rec_phone);
        franApp.setSearch(franApp.toString());
        franApp.setFollowerId(followerId);
        franApp.setCreateTime(DataConvert.convert(dtApp));

        if (franAppMapper.saveFranApp(franApp) > 0) {
            return DataConvert.toJson(ResultUtil.success(), callback);
        } else {
            return DataConvert.toJson(ResultUtil.error(), callback);
        }
    }

    @RequestMapping(value = "/listFranAppChannel")
    private Object listFranAppChannel(HttpServletResponse rsp,@RequestParam(value = "callback",required = false) String callback){
//        rsp.addHeader("Access-Control-Allow-Origin", "*");
//        rsp.setHeader("Content-Type", "application/json;charset=UTF-8");
        List<HashMap> channes=franAppMapper.listFranAppChannel();
        return  DataConvert.toJson(ResultUtil.success(channes));
    }

    @RequestMapping(value = "/listFranApp")
    private Object listFranApp(HttpServletResponse rsp,@RequestParam(value = "callback",required = false) String callback,
                               @RequestParam(value = "keyWord",defaultValue = "") String keyWord,
                               @RequestParam(value = "userid",required = false) Integer userid,
                               @RequestParam(value = "dtBegin",defaultValue = "") String dtBegin,
                               @RequestParam(value = "dtEnd",defaultValue = "") String dtEnd,
                               @RequestParam(value = "sort",defaultValue = "dt desc") String sort,
                               @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNow,
                               @RequestParam(value = "pageSize", defaultValue = "30") Integer size,
                               @RequestParam(value = "todayFollow",defaultValue = "0") String todayFollow,
                               @RequestParam(value = "unAllocate",defaultValue = "0") String unAllocate,
                               @RequestParam(value = "advSearch",required = false) String advSearch,
                               @RequestParam(value = "advSearch2",required = false) String advSearch2){

//        rsp.addHeader("Access-Control-Allow-Origin", "*");
//        rsp.setHeader("Content-Type", "application/json;charset=UTF-8");

        List<HashMap> apps = franAppMapper.listFranApp(userid,dtBegin,dtEnd,DataConvert.decode(keyWord),size,pageNow,sort,todayFollow,advSearch,advSearch2,unAllocate);
        return DataConvert.toJson(ResultUtil.success(apps),callback);
    }


    @RequestMapping(value = "/updateFranApp")
    public Object updateAppli(HttpServletResponse rsp,@RequestParam(value = "callback",required = false) String callback,
                              @RequestParam(value = "id",required = true) Integer id,
                              @RequestParam(value = "name",defaultValue = "") String name,
                              @RequestParam(value = "phone",defaultValue = "") String phone,
                              @RequestParam(value = "wechatName",defaultValue = "") String wechatName,
                              @RequestParam(value = "email",defaultValue = "") String email,
                              @RequestParam(value = "channel",defaultValue = "") String channel,
                              @RequestParam(value = "address",defaultValue = "") String address,
                              @RequestParam(value = "dt",defaultValue = "") String dt,
                              @RequestParam(value = "nextTime",defaultValue = "") String nextTime,
                              @RequestParam(value = "linkTime",required = false) Integer linkTime,
                              @RequestParam(value = "status",required = false) Integer status){

//        rsp.addHeader("Access-Control-Allow-Origin", "*");
//        rsp.setHeader("Content-Type", "application/json;charset=UTF-8");
        if (id.equals("")||id==null) {
            return DataConvert.toJson(ResultUtil.error("没有记录ID"), callback);
        }
        if (franAppMapper.updateFranApp(id,name,phone,email,channel,address,dt,nextTime,linkTime,status,wechatName) > 0) {
            return DataConvert.toJson(ResultUtil.success(), callback);
        } else {
            return DataConvert.toJson(ResultUtil.error(), callback);
        }
    }
    @RequestMapping(value = "/updateFranApps")
    public Object updateApplis(HttpServletResponse rsp,@RequestParam(value = "callback",required = false) String callback,
                            @RequestParam(value = "ids[]",required = true) String[] ids,
                            @RequestParam(value = "FollowerID",required = true) Integer FollowerID){
//        rsp.addHeader("Access-Control-Allow-Origin", "*");
//        rsp.setHeader("Content-Type", "application/json;charset=UTF-8");

        if (ids.equals("")||ids==null) {
            return DataConvert.toJson(ResultUtil.error("没有记录ID"), callback);
        }
        if (franAppMapper.updateFranApps(ids,FollowerID) > 0) {
            return DataConvert.toJson(ResultUtil.success(), callback);
        } else {
            return DataConvert.toJson(ResultUtil.error(), callback);
        }
    }
    @RequestMapping(value = "/deleteFranApp")
    private Object deleteFranApp(HttpServletResponse rsp,@RequestParam(value = "callback",required = false) String callback,
                                 @RequestParam(value = "ids[]", required = true) String[] ids){

//        rsp.addHeader("Access-Control-Allow-Origin", "*");
//        rsp.setHeader("Content-Type", "application/json;charset=UTF-8");
        Integer res =franAppMapper.deleteFranApp(ids);
        if(res==null) return DataConvert.toJson(ResultUtil.error(),callback);
        return DataConvert.toJson(ResultUtil.success(),callback);

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
//        rsp.addHeader("Access-Control-Allow-Origin", "*");
//        rsp.setHeader("Content-Type", "application/json;charset=UTF-8");
        log.info(ids.toString());
        if (ids == null) {
            return ResultUtil.error(-1, "Intro not selected");
        }

        if(introMapper.handleIntro(ids)>0) {
            return DataConvert.toJson(ResultUtil.success(),callback);
        }else {
            return DataConvert.toJson(ResultUtil.error(),callback);

        }

    }


    @RequestMapping(value = "/syncGym")
    public ModelAndView syncGym(@RequestBody String param, Map<String, Object> res, HttpServletResponse rsp) {
//        rsp.setHeader("Content-Type", "text/xml;charset=UTF-8");
//        rsp.addHeader("Access-Control-Allow-Origin", "*");
        //String soap="<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><soap:Body><mGetOutMessage xmlns=\"http://tempuri.org/crm_server/Crm_OutMessage\"><outobject><UserID>279833</UserID><ID>4861</ID><SessionID /><ObjectName>crm_zdytable_238592_26277</ObjectName><fields><sobjectField><fieldsign>手机号</fieldsign><fieldname>crmzdy_81762775</fieldname><fieldvalue>test</fieldvalue><fieldtype>文本</fieldtype></sobjectField><sobjectField><fieldsign>短信内容</fieldsign><fieldname>crmzdy_81762774</fieldname><fieldvalue>stes</fieldvalue><fieldtype>文本</fieldtype></sobjectField><sobjectField><fieldsign>中心编号</fieldsign><fieldname>crmzdy_81762776</fieldname><fieldvalue>500012</fieldvalue><fieldtype>文本</fieldtype></sobjectField><sobjectField><fieldsign>记录ID</fieldsign><fieldname>id</fieldname><fieldvalue>4861</fieldvalue><fieldtype>整数</fieldtype></sobjectField></fields></outobject></mGetOutMessage></soap:Body></soap:Envelope>";
        //初始化报文，调用parse方法，获得结果map
        try {
            XmlUtil xml = new XmlUtil(param);
            res.put("id", xml.getVal("ID"));
            System.out.println(xml.getRes());
            Gym gym = new Gym();
            gym.setId(xml.getVal("crmzdy_80620116"));
            gym.setCH_Name(xml.getVal("crm_name"));
            gym.setCity(xml.getVal("crmzdy_81744959"));
//            City city = cityMapper.getCity(xml.getVal("crmzdy_81744959"));
//            gym.setCityId(city.getId());
            gym.setProv(xml.getVal("crmzdy_81744958"));
            gym.setPhone(xml.getVal("crmzdy_80616967"));
            gym.setFax(xml.getVal("crmzdy_80620117"));
            gym.setEmail(xml.getVal("crmzdy_80620118"));
            gym.setYYEmail(xml.getVal("crmzdy_82048062"));
            gym.setAddr(xml.getVal("crmzdy_80616968"));
            gym.setCoordinate(xml.getVal("crmzdy_82040405"));
            gym.setTip(xml.getVal("crmzdy_81765917"));
            gym.setServer(xml.getVal("crmzdy_82058140"));
            gym.setAppId(xml.getVal("crmzdy_81762517"));
            gym.setAppKey(xml.getVal("crmzdy_81762518"));
            gym.setApp_signature(xml.getVal("crmzdy_81762709"));
            gym.setIsPreparing(xml.getVal("crmzdy_87673553"));
            Date create_time = DataConvert.convert(xml.getVal("create_time"), "yyyy/MM/dd hh:mm:ss");
            gym.setCreateTime(create_time);
            Date dtPreSale = DataConvert.convert(xml.getVal("crmzdy_82011756"), "yyyy/MM/dd hh:mm:ss");
            gym.setDtPreSale(dtPreSale);
            Date dtOpen = DataConvert.convert(xml.getVal("crmzdy_82011760"), "yyyy/MM/dd hh:mm:ss");
            gym.setDtOpen(dtOpen);
            gym.setStatus(xml.getVal("crmzdy_87676789"));
            gymMapper.saveGym(gym);

        } catch (Exception e) {
            res.put("isSuccess", "false");
            res.put("msg", "未知错误");
            e.printStackTrace();
            return new ModelAndView("message/error", res);
        }
        res.put("isSuccess","true");
        return new ModelAndView("message/success", res);

    }

    @RequestMapping(value = "/sendSMS")
    public ModelAndView sendSMS(@RequestBody String param, Map<String, Object> res, HttpServletResponse rsp) {
//        rsp.setHeader("Content-Type", "text/xml;charset=UTF-8");
//        rsp.addHeader("Access-Control-Allow-Origin", "*");
        String id="",tb="";
        try {
            XmlUtil xml = new XmlUtil(param);
            tb=xml.getVal("ObjectName");
            id= xml.getVal("ID");
            System.out.println(xml.getRes());
            //String appid = "22870";
            //String appkey = "185364fe9e7f25f8ab2bb979505cad27";
            String gymCode="";
            String phone="";
            String content="";
            if(tb.equals("crm_zdytable_238592_26277")) {
                //原短信表接口
                gymCode = xml.getVal("crmzdy_81762776");
                phone = xml.getVal("crmzdy_81762775");
                content = xml.getVal("crmzdy_81762774");
            }else {
                gymCode = xml.getVal("crmzdy_81762900");
                phone = xml.getVal("crmzdy_81762850");
                content = xml.getVal("crmzdy_81748934");
            }

            Gym gym=gymMapper.findGym(gymCode);
            PhoneMsg phoneMsg =new PhoneMsg(gym.getAppId(),gym.getAppKey());
            phoneMsg.setPhone(phone);
            phoneMsg.setContent("【"+gym.getApp_signature()+"】"+content);
            if(gym!=null && gym.getServer().equals("赛邮")
                    &&!gym.getAppId().equals("")&&!gym.getAppKey().equals("")) {
                //System.out.println(phoneMsg);
                res = iSmsService.synchPhoneMsg(phoneMsg);
            }else{
                res.put("isSuccess", "false");
                res.put("code", -2);
                res.put("msg", "赛邮短信设置不正确");
            }
            res.put("id",id);
        } catch (Exception e) {
            res.put("isSuccess", "false");
            res.put("code", -1);
            res.put("msg", e.getMessage());
            e.printStackTrace();
        }
        //log.info("res:{}",res);
        if(res.get("isSuccess").equals("true")) {
            if(!tb.equals("crm_zdytable_238592_26277")) {
                iSynchBackService.synchGt(id);
            }
            return new ModelAndView("message/success", res);
        }else{
            return new ModelAndView("message/error", res);
        }
    }

    @RequestMapping(value = "/saveFranApps", method = RequestMethod.POST)
    public Object createAppoint(HttpServletResponse rsp, @RequestBody JSONObject data) {
//        rsp.setHeader("Content-Type", "text/xml;charset=UTF-8");
//        rsp.addHeader("Access-Control-Allow-Origin", "*");
//        rsp.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
        Integer res=franAppMapper.saveFranApps(data.getString("sql"));
        if(res>0) {
            return DataConvert.toJson(ResultUtil.success("导入"+res.toString()+"条"));
        }else{
            return DataConvert.toJson(ResultUtil.error("导入0条，导入失败"));
        }
    }

}

