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
    private int id;
    public String phone;
    private String appid;
    private String appkey;
    public String content;

    public PhoneMsg(String appid, String appkey) {
        this.appid = appid;
        this.appkey = appkey;
    }
    public PhoneMsg() {
    }
    @Override
    public String toString() {
        StringBuilder strb = new StringBuilder();
        strb.append("{");
        strb.append("'appid':'" + appid + "'");
        strb.append(",'signature':'" + appkey + "'");
        strb.append(",'to':'" + phone + "'");
        strb.append(",'content':'" + content + "'");
        strb.append("}");
        return strb.toString();
    }


}
