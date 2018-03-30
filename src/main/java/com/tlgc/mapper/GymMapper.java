package com.tlgc.mapper;


import com.tlgc.entity.City;
import com.tlgc.entity.Gym;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by TONY on 2017/11/19.
 */
public interface GymMapper {
    @Select("select Id,CH_Name from TLG_Gym where status=1 and CityId=${CityId} or ${CityId}=-1 order by 2")
    public List<Gym> getAllByCityId(@Param("CityId") Integer CityId);

    @Select("select Id,CH_Name,phone,email,coordinate,tip,addr from TLG_Gym where status=1 and City=#{City} order by 2")
    public List<Gym> getAllByCity(@Param("City") String City);

}
