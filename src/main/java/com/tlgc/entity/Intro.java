package com.tlgc.entity;

import lombok.Data;

import javax.persistence.Id;
import java.util.Date;
/**
 * Created by TONY on 2017/11/19.
 */
@Data
public class Intro {
    @Id
    private Integer Id;
    private Integer Center;
    private Integer City;
    private String Province;
    private String BabyName;
    private String BabyBirthday;
    private String Email;
    private String ParentPhone;
    private String Channel;
    private Integer MailStatus;
    private Date CreateTime;
    private String search;

    @Override
    public String toString() {
        return "Intro{" +
                "Id=" + Id +
                ", Center=" + Center +
                ", City=" + City +
                ", Province='" + Province + '\'' +
                ", BabyName='" + BabyName + '\'' +
                ", BabyBirthday='" + BabyBirthday + '\'' +
                ", Email='" + Email + '\'' +
                ", ParentPhone='" + ParentPhone + '\'' +
                ", Channel='" + Channel + '\'' +
                ", MailStatus=" + MailStatus +
                ", CreateTime=" + CreateTime +
                '}';
    }
}
