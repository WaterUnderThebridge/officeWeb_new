package com.tlgc.entity;

//import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;
//import net.sf.json.JSONObject;
///==============================



/**
 * Created by hibernate on 2018/5/24.
 */
@Data
@Entity
public class PhoneMsg {
    @Id
    private  int id;
    public String phone ;
    private String appid="22870";
    private String appkey="185364fe9e7f25f8ab2bb979505cad27";
    public String content;

@Override
public String toString() {
//    JSONObject jsonObject=new JSONObject();
//    jsonObject.put("appid",appid);
//    jsonObject.put("to",phone);
//    jsonObject.put("content",content);
//    jsonObject.put("signature",appkey);
//    return  jsonObject.toString();
    StringBuilder strb=new StringBuilder();
    strb.append("{");
    strb.append("'appid':'13698'");
    strb.append(",'signature':'3877871b96eac711ed8a76e286df30bc'");
    strb.append(",'to':'"+phone+"'");
    strb.append(",'content':'"+content+"'");
    strb.append("}");
    return strb.toString();
}
//    public JSONObject toJson(){
//        JSONObject jsonObject=new JSONObject();
//        jsonObject.put("appid",appid);
//        jsonObject.put("to",phone);
//        jsonObject.put("content",content);
//        jsonObject.put("signature",appkey);
//        return  jsonObject;
//    }
    public void PhoneMsg(){
        phone="";
        appid="22870";
        appkey="185364fe9e7f25f8ab2bb979505cad27";
        content="";
    }
}
