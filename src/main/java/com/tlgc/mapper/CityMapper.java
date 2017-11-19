package com.tlgc.mapper;


import com.tlgc.entity.City;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by TONY on 2017/11/19.
 */
public interface CityMapper {
    @Select("select top 1 M_Id Id,CH_Name from TLG_City where Id=${Id}")
    public City getOne(@Param("Id") Integer Id);
    @Select("select  M_Id Id,CH_Name from TLG_City where ProvinceId=${ProvinceId}")
    public List<City> getAllByProvinceId(@Param("ProvinceId") Integer ProvinceId);
}
