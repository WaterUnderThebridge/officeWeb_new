package com.tlgc.Convertor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tlgc.entity.Result;



import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * 全局日期处理类
 * Convert<T,S>
 *         泛型T:代表客户端提交的参数 String
 *         泛型S:通过convert转换的类型

 */
public class DataConvert {
    public static String timeStamp2Date(String seconds,String format) {
           if(seconds == null || seconds.isEmpty() || seconds.equals("null")){
                   return "";
               }
           if(format == null || format.isEmpty()){
                   format = "yyyy-MM-dd HH:mm:ss";
               }
           SimpleDateFormat sdf = new SimpleDateFormat(format);
           return sdf.format(new Date(Long.valueOf(seconds+"000")));
     }
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
    public static String stripHtml(String content) {
        // <p>段落替换为换行
        content = content.replaceAll("<p .*?>", "\r\n");
        // <br><br/>替换为换行
        content = content.replaceAll("<br\\s*/?>", "\r\n");
        // 去掉其它的<>之间的东西
        content = content.replaceAll("\\<.*?>", "");
        // 去掉空格
        content = content.replaceAll(" ", "");
        return content;
    }
    public static String delHtmlTag(String str){
        String newstr = "";
        newstr = str.replaceAll("<[.[^>]]*>","");
        newstr = newstr.replaceAll(" ", "");
        return newstr;
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

    public static String makeParameters(Object obj, String encoding) {
           return  makeParameters(obj2Map(obj),encoding);
    }
    public static String makeParameters(Object obj) {
        return  makeParameters(obj2Map(obj),"UTF-8");
    }
    public static String makeParameters(Map<String, Object> parameters, String encoding) {
        // 判断参数集合是否有效
        if (parameters.isEmpty()) {
            return "";
        }

        StringBuilder s = new StringBuilder();
        char spliter = 0;

        // 遍历参数集合
        for (Map.Entry<String, Object> e : parameters.entrySet()) {
            // 写入参数分隔符
            if (spliter == 0) {
                spliter = '&';
            } else {
                s.append(spliter);
            }
            // 写入参数名
            s.append(e.getKey());
            s.append("=");
            try {
                // 写入参数值
                String value="";
                if(e.getValue() instanceof Date){
                    value = convert((Date)e.getValue());
                }else {
                    value = String.valueOf(e.getValue());
                }
                s.append(value == null ? "" : URLEncoder.encode(value, encoding));
            } catch (UnsupportedEncodingException e1) {
                throw new IllegalArgumentException(encoding);
            }
        }
        // 返回参数字符串
        return s.toString();
    }

    public static Map<String, Object> obj2Map(Object obj) {
        Map<String, Object> map = new HashMap<String, Object>();
        // System.out.println(obj.getClass());
        // 获取f对象对应类中的所有属性域
        Field[] fields = obj.getClass().getDeclaredFields();
        for (int i = 0, len = fields.length; i < len; i++) {
            String varName = fields[i].getName();
            varName = varName.toLowerCase();//将key置为小写，默认为对象的属性
            try {
                // 获取原来的访问控制权限
                boolean accessFlag = fields[i].isAccessible();
                // 修改访问控制权限
                fields[i].setAccessible(true);
                // 获取在对象f中属性fields[i]对应的对象中的变量
                Object o = fields[i].get(obj);
                if (o != null)
                    map.put(varName, o.toString());
                // System.out.println("传入的对象中包含一个如下的变量：" + varName + " = " + o);
                // 恢复访问控制权限
                fields[i].setAccessible(accessFlag);
            } catch (IllegalArgumentException ex) {
                ex.printStackTrace();
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
            }
        }
        return map;
    }


    public static String decode(String unicodeStr) {
        if (unicodeStr == null) {
            return null;
        }
        StringBuilder retBuf = new StringBuilder();
        int maxLoop = unicodeStr.length();
        for (int i = 0; i < maxLoop; i++) {
            if (unicodeStr.charAt(i) == '\\') {
                if ((i < maxLoop - 5)
                        && ((unicodeStr.charAt(i + 1) == 'u') || (unicodeStr
                        .charAt(i + 1) == 'U')))
                    try {
                        retBuf.append((char) Integer.parseInt(unicodeStr.substring(i + 2, i + 6), 16));
                        i += 5;
                    } catch (NumberFormatException localNumberFormatException) {
                        retBuf.append(unicodeStr.charAt(i));
                    }
                else {
                    retBuf.append(unicodeStr.charAt(i));
                }
            } else {
                retBuf.append(unicodeStr.charAt(i));
            }
        }
        return retBuf.toString();
    }

}