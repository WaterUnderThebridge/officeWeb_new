package com.tlgc.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by hibernate on 2017/4/13.
 */

@Data
@Table(name="TLG_Gym")
public class Gym {
    @Id
    private String Id;
    private String CH_Name;
    private Integer cityId;
    private String prov;
    private String city;
    private String phone;
    private String email;
    private String YYEmail;
    private String coordinate;
    private String tip;
    private String fax;
    private String addr;
    private String appId;
    private String appKey;
    private String server;
    private String app_signature;
    private Date  dtOpen;
    private Date  dtPreSale;
    private String status;
}
