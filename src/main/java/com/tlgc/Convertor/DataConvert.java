package com.tlgc.Convertor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPObject;

import com.tlgc.entity.Result;
import lombok.extern.slf4j.Slf4j;


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
@Slf4j
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
            return JSONObject.toJSONString(jsonpObject);
        }
    }

    public static String toJsonStr(Object object,String function) {
       String result=JSONObject.toJSONString(object);
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
    /**
     * 解析出url参数中的键值对
     * 如 "index.jsp?Action=del&id=123"，解析出Action:del,id:123存入map中
     *
     * @param URL url地址
     * @return url请求参数部分
     */
    public static Map<String, String> URLRequest(String URL) {
        Map<String, String> mapRequest = new HashMap<String, String>();
        if(URL==null || URL=="") return mapRequest;
        String[] arrSplit = null;
        String strUrlParam = TruncateUrlPage(URL);
        if (strUrlParam == null) {
            return mapRequest;
        }
        //每个键值为一组 www.2cto.com
        arrSplit = strUrlParam.split("[&]");
        for (String strSplit : arrSplit) {
            String[] arrSplitEqual = null;
            arrSplitEqual = strSplit.split("[=]");
            //解析出键值
            if (arrSplitEqual.length > 1) {
                //正确解析
                mapRequest.put(arrSplitEqual[0], arrSplitEqual[1]);
            } else {
                if (arrSplitEqual[0] != "") {
                    //只有参数没有值，不加入
                    mapRequest.put(arrSplitEqual[0], "");
                }
            }
        }
        return mapRequest;
    }

    private static String TruncateUrlPage(String strURL) {
        String strAllParam = null;
        String[] arrSplit = null;
        strURL = strURL.trim();
        if(strURL.indexOf("?")==-1){
            return strURL;
        }
        arrSplit = strURL.split("[?]");
        if (strURL.length() > 1) {
            if (arrSplit.length > 1) {
                if (arrSplit[1] != null) {
                    strAllParam = arrSplit[1];
                }
            }
        }
        return strAllParam;
    }

}