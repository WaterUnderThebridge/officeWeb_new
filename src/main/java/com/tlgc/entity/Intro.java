package com.tlgc.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;
/**
 * Created by TONY on 2017/11/19.
 */
@Data
@Entity
public class Intro {
    @Id
    private Integer Id;
    private String Center;
    private String City;
    private String gymCode;
    private String Province;
    private String BabyName;
    private String BabyBrithday;
    private String Email;
    private String ParentPhone;
    private String Channel;
    private Integer MailStatus;
    private Date CreateTime;
    private Integer status;
    private String search;
    private int isSync;
    @Override
    public String toString() {

        String tmp =
                ", BabyName='" + BabyName + '\'' +
                ", BabyBirthday='" + BabyBrithday + '\'' +
                ", Email='" + Email + '\'' +
                ", ParentPhone='" + ParentPhone + '\'' +
                ", Channel='" + Channel + '\'' +
                ", Email=" + Email +
                ", CreateTime=" + CreateTime+
                "}";

        return (status==3)? tmp+"已处理":tmp+ "未处理";
    }
}
