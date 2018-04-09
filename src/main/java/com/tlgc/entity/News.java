package com.tlgc.entity;

import lombok.Data;
import lombok.Generated;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by TONY on 2018/4/4.
 */
@Data
@Entity
@Table(name="TLG_NewsCenter")
public class News {
    @Id
    @Generated
    private Integer Id;
    private Integer Type;
    private Integer LanguageType;
    private String Title ;
    private String HeadPic;
    private String Summary;
    private String Content;
    private Integer Sort;
    private Date CreateTime = new Date();
    private Date UpdateTime = new Date();
    private Integer Status;
}
