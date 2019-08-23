package com.tlgc.mapper;


import com.tlgc.entity.City;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by TONY on 2017/11/19.
 */
public interface CityMapper {
    @Select("select top 1 M_Id Id,CH_Name from TLG_City where status=2 and charindex(CH_Name,#{CH_Name})>0")
    public City getCity(@Param("CH_Name") String CH_Name);
    @Select("select ProvinceId,M_Id Id,CH_Name from TLG_City where status=2 and ProvinceId=#{ProvinceId} " +
            "union all select ProvinceId,M_Id Id,CH_Name from TLG_City where status=2 and -1=#{ProvinceId} order by ProvinceId")
    public List<City> getAllByProvinceId(@Param("ProvinceId") Integer ProvinceId);
}
