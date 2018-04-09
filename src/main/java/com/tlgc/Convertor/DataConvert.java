package com.tlgc.Convertor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tlgc.entity.Result;
import com.tlgc.utils.ResultUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * 全局日期处理类
 * Convert<T,S>
 *         泛型T:代表客户端提交的参数 String
 *         泛型S:通过convert转换的类型
 */
public class DataConvert {

    public static Date convert(String stringDate, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        try {
            return simpleDateFormat.parse(stringDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static Date convert(String stringDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return simpleDateFormat.parse(stringDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String convert(Date dt, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(dt);
    }
    public static String convert(Date dt) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(dt);
    }
    //去除了null字段
    public static JSONObject toJson(Result result) {
        return JSON.parseObject(JSON.toJSONString(result));
    }
    public static Object toJson(Result result, String function) {
        if(function==null){
            return result;
        }else{
            JSONPObject jsonpObject= new JSONPObject(function);
            jsonpObject.addParameter(result);
            return jsonpObject;
        }
    }

    public static String toJsonStr(Object object,String function) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.disableHtmlEscaping().create();
        String result = gson.toJson(object);

        if(function!=null) {
            result = new StringBuilder(function).append("(").append(result).append(")").toString();
        }
        return  result;
    }

}
