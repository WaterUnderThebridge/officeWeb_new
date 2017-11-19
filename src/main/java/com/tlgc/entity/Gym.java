package com.tlgc.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by hibernate on 2017/4/13.
 */

@Data
@Table(name="LG_Enterprise")
public class Gym {
    @Id
    private Integer Id;
    private String CH_Name;
    private Integer CityId;
    private Integer status;
}
