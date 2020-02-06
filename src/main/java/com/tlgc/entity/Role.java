package com.tlgc.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by TONY on 2017/9/17.
 */
@Data
@Table(name="TLG_Roles")
public class Role {
    @Id
    @GeneratedValue
    private Integer id;
    private String  name;
    private String  title;
    private String  acl;
}
