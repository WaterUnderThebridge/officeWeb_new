package com.tlgc.entity;

import lombok.Data;


import java.util.Date;
import javax.persistence.*;
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
    private String BabyBrithday;
    private String Email;
    private String ParentPhone;
    private String Channel;
    private Integer MailStatus;
    private Date CreateTime;
}
