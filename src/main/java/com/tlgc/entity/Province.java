package com.tlgc.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by TONY on 2017/11/18.
 */
@Entity
@Data
@Table(name="TLG_Province")
public class Province {
    @Id
    private Integer Id;
 
    private String CH_Name;
 
    private String EN_Name;
 
    private Integer Sort;
 
    private Integer Status;
 
    private Integer Type;
}




