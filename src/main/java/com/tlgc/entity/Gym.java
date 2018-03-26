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
@Table(name="TLG_Gym")
public class Gym {
    @Id
    private Integer code;
    private String name;
    private Integer cityId;
    private byte status;
}
