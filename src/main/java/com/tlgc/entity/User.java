package com.tlgc.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by TONY on 2019/6/20.
 */
@Data
@Entity
@Table(name="TLG_User")
public class User {
    @Id
    @GeneratedValue
    private Integer id;
    private String  username;
    private String  password;
    private String  fullname;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roleId", nullable = true)
    private Role role;
    private String token;
    private Date updateTime = new Date();
    private Date createTime = new Date();
    private Integer isdelete;

}
