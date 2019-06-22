package com.tlgc.entity;

import lombok.Data;
import lombok.Generated;
import org.springframework.format.annotation.DateTimeFormat;

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
    private String rec_phone;
    private String rec_name;
    private String Remark ="";
    private Integer LinkTime;
    private String nextTime;
    private Date UpdateTime =new Date();
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
