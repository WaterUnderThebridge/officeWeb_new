package com.tlgc.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by hibernate on 2017/5/19.
 */

@Data
@Table(name="TLG_Admin")
public class Admin {
    @Id
    @GeneratedValue
    private Integer id;
    private String username;
    private String password;
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roleId", nullable = true)
    private Role role;
    private Date createTime;
    private boolean isDelete;
    private String shit;

}
