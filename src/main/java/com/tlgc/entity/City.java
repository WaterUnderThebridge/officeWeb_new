package com.tlgc.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * Created by TONY on 2017/11/18.
 */
@Entity
@Data
@Table(name="TLG_City")
public class City {
    @Id
    @Column(name="M_Id")
    private Integer Id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ProvinceId", nullable = true)
    private Province province;
    private String CH_Name;
    private String EN_Name;
    private Integer Sort;
    private Integer Status;

}






