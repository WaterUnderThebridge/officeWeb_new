package com.tlgc.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by TONY on 2018/4/11.
 */
@Data
@Entity
public class FranApp {
    @Id
    @GeneratedValue
    private Integer Id;
    private String Name;
    private String Phone;
    private String wechatName;
    private String Email;
    private String Address;
    private String Channel;
    private String rec_phone;
    private String rec_name;
    private String Remark ="";
    private Integer followerId;
    private Integer LinkTime;
    private String nextTime;
    private String dtMeetUp;
    private String amtInvest;
    private Date UpdateTime =new Date();
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date CreateTime =new Date();
    private Integer Status=1;
    private Integer MailStatus;
    private String Search;

    @Override
    public String toString() {
        return "FranApp{" +
                "Name='" + Name + '\'' +
                ", Phone='" + Phone + '\'' +
                ", Email='" + Email + '\'' +
                ", Channel='" + Channel + '\'' +
                ", Address='" + Address + '\'' +
                ", Remark='" + Remark + '\'' +
                ", CreateTime=" + CreateTime +
                '}';
    }
}
