package com.tlgc.service.impl;
import com.alibaba.fastjson.JSONObject;
import com.tlgc.entity.PhoneMsg;
import com.tlgc.service.ISmsService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//////////////////////////////////////////////////////////////
import java.io.IOException;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Map;
import com.tlgc.lib.RequestEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;


/**
 * Created by hibernate on 2018/5/28.
 */
@Slf4j
@Service
public class SmsServiceImpl implements ISmsService {
    @Autowired
    SmsServiceImpl iPhoneMsg;


    public static final String TIMESTAMP = "https://api.mysubmail.com/service/timestamp";


    public static final String TYPE_MD5 = "md5";
    public static final String TYPE_SHA1 = "sha1";

    private static final String URL="https://api.mysubmail.com/message/send.json";

    @Override
    public Map<String, Object>  synchPhoneMsg(PhoneMsg phoneMsg){
        //log.info("Json："+phoneMsg.toString());
        TreeMap<String, Object> requestData = new TreeMap<String, Object>();
        /**
         * --------------------------------参数配置------------------------------------
         * 请仔细阅读参数配置说明
         *
         * 配置参数包括 appid, appkey ,to , signtype, 	content
         * 用户参数设置，其中 appid, appkey, content, to 为必须参数
         * 请访问 submail 官网创建并获取 appid 和 appkey，链接：https://www.mysubmail.com/chs/sms/apps
         * 请访问 submail 官网创建获取短信模板内容，链接：https://www.mysubmail.com/chs/sms/templates
         * content 参数
         *   |正文中必须提交有效的短信签名，且您的短信签名必须放在短信的最前端，e.g. 【SUBMAIL】您的短信验证码：4438，请在10分钟内输入。
         *   |content 参数将会与您账户中的短信模板进行匹配，如 API 返回 420 错误，请在您的账户中添加短信模板，并提交审核
         *   |请将 短信正文控制在 350 个字符以内
         * signtype 为可选参数: normal | md5 | sha1
         * 当 signtype 参数为空时，默认为 normal
         * --------------------------------------------------------------------------
         */
        String appid =phoneMsg.getAppid();
        log.info(appid);

        String appkey =phoneMsg.getAppkey();
        log.info(appkey);

        String to =phoneMsg.getPhone();
        log.info(to);
        String content =phoneMsg.getContent();
        //log.info("短信内容:"+content);
        String signtype = "normal";
        /**
         *  ---------------------------------------------------------------------------
         */
        /**
         *  签名验证方式
         *  详细说明可参考 SUBMAIL 官网，开发文档 → 开始 → API 授权与验证机制
         */
        requestData.put("appid", appid);
        requestData.put("content", content);
        requestData.put("to", to);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        @SuppressWarnings("deprecation")
        ContentType contentType = ContentType.create(HTTP.PLAIN_TEXT_TYPE,HTTP.UTF_8);
        for(Map.Entry<String, Object> entry: requestData.entrySet()){
            String key = entry.getKey();
            Object value = entry.getValue();
            if(value instanceof String){
                builder.addTextBody(key, String.valueOf(value),contentType);
            }
        }
        if(signtype.equals(TYPE_MD5) || signtype.equals(TYPE_SHA1)){
            String timestamp = getTimestamp();
            requestData.put("timestamp", timestamp);
            requestData.put("sign_type", signtype);
            String signStr = appid + appkey + RequestEncoder.formatRequest(requestData) + appid + appkey;
            System.out.println(signStr);
            builder.addTextBody("timestamp", timestamp);
            builder.addTextBody("sign_type", signtype);
            builder.addTextBody("signature", RequestEncoder.encode(signtype, signStr), contentType);//原
        }else{
            builder.addTextBody("signature", appkey, contentType);
        }
        /**
         * http post 请求接口
         * 成功返回 status: success,其中 fee 参数为短信费用 ，credits 参数为剩余短信余额
         * 详细的 API 错误日志请访问 SUBMAIL 官网 → 开发文档 → DEBUG → API 错误代码
         */
        HttpPost httpPost = new HttpPost(URL);
        httpPost.addHeader("charset", "UTF-8");
        httpPost.setEntity(builder.build());
        Map<String, Object> res=new HashMap<>();
        try{
            CloseableHttpClient closeableHttpClient = HttpClientBuilder.create().build();
            HttpResponse response = closeableHttpClient.execute(httpPost);
            HttpEntity httpEntity = response.getEntity();
            if(httpEntity != null){
                String jsonStr = EntityUtils.toString(httpEntity, "UTF-8");
                JSONObject json=JSONObject.parseObject(jsonStr);
                //System.out.println(jsonStr);
                //log.info("转换的值："+json);
                String s=json.get("status").toString();
                if(s.equals("error")){
                    res.put("isSuccess", "false");
                    res.put("msg",json.get("msg").toString());
                    res.put("code",json.get("code").toString());
                }else{
                    res.put("isSuccess", "true");
                }
            }
        }catch(ClientProtocolException e){
            e.printStackTrace();
            res.put("isSuccess", "false");
            res.put("msg",e.getMessage());
            res.put("code",-1);
        }catch(IOException e){
            e.printStackTrace();
            res.put("isSuccess", "false");
            res.put("msg",e.getMessage());
            res.put("code",-1);
        }
        return  res;
    }

    /**
     * 获取时间戳
     * @return
     */
    private static String getTimestamp(){
        CloseableHttpClient closeableHttpClient = HttpClientBuilder.create().build();
        HttpGet httpget = new HttpGet(TIMESTAMP);
        try{
            HttpResponse response = closeableHttpClient.execute(httpget);
            org.apache.http.HttpEntity httpEntity = response.getEntity();
            String jsonStr = EntityUtils.toString(httpEntity,"UTF-8");
            if(jsonStr != null){
                JSONObject json = JSONObject.parseObject(jsonStr);
                return json.getString("timestamp");
            }
            closeableHttpClient.close();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


}
