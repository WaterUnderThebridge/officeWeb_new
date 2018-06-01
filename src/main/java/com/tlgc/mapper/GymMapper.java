package com.tlgc.mapper;


import com.tlgc.entity.City;
import com.tlgc.entity.Gym;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by TONY on 2017/11/19.
 */
public interface GymMapper {
    @Select("select Id,CH_Name,dtPreSale,dtOpen from TLG_Gym where status=1 and CityId=${CityId} or ${CityId}=-1 order by 2")
    public List<Gym> getAllByCityId(@Param("CityId") Integer CityId);

    @Select("select prov,city,Id,CH_Name name,phone,email,YYEmail,coordinate,tip,addr,dtPreSale,dtOpen from TLG_Gym where status=1 and (City=#{City} or #{City}=-1) order by 2")
    public List<HashMap> getAllByCity(@Param("City") String City);

    @Select("select prov,city,Id,CH_Name name,phone,email,YYEmail,coordinate,tip,addr,dtPreSale,dtOpen,server,appId,appKey from TLG_Gym where id=#{id}")
    public Gym findGym(@Param("id") String id);

    @Insert("delete from tlg_gym where Id=#{Id};insert into tlg_gym(Id,CH_Name,prov,city,cityId,phone,fax,email,YYEmail,coordinate,tip,addr,dtPreSale,dtOpen,updateTime,status,server,appId,appKey)values" +
            "(#{Id},#{CH_Name},#{prov},#{city},#{cityId},#{phone},#{fax},#{email},#{YYEmail},#{coordinate},#{tip},#{addr},#{dtPreSale},#{dtOpen},getdate(),#{status},#{server},#{appId},#{appKey})")
    public Integer saveGym(Gym gym);

}
