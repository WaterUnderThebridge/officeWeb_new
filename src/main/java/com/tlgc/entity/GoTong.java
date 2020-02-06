package com.tlgc.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by TONY on 2019/6/27.
 */
@Data
@Table(name="TLG_Gotong")
public class GoTong {
    @Id
    @GeneratedValue
    private Integer Id;
    private Integer franAppId;
    private Integer userId;
    private String content;
    private Integer isdelete=0;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date dtGotong;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date createTime=new Date();
}
