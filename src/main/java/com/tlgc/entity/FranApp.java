package com.tlgc.entity;

import lombok.Data;
import lombok.Generated;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by TONY on 2018/4/11.
 */
@Data
@Entity
public class FranApp {
    @Id
    @Generated
    private Integer Id;
    private String Name;
    private String Phone;
    private String Email;
    private String Address;
    private String Channel;
    private String Remark ="";
    private Integer LinkTime;
    private Date CreateTime =new Date();
    private Integer Status=1;
    private Integer MailStatus;
    private String Search;

    @Override
    public String toString() {
        String tmp= "FranApp{" +
                "Name='" + Name + '\'' +
                ", Phone='" + Phone + '\'' +
                ", Email='" + Email + '\'' +
                ", Channel='" + Channel + '\'' +
                ", Address='" + Address + '\'' +
                ", Remark='" + Remark + '\'' +
                ", CreateTime=" + CreateTime +
                '}';

        return (Status==3)? tmp+"已处理":tmp+ "未处理";
    }
}
