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
    @Select("select Id,name CH_Name,dtPreSale,dtOpen from TLG_Gym where status=1 and (CityId=${CityId} or ${CityId}=-1) order by 2")
    public List<Gym> getAllByCityId(@Param("CityId") Integer CityId);

    @Select("select prov,city,Id,name,phone,email,YYEmail,coordinate,tip,addr,dtPreSale,dtOpen from TLG_Gym where status=1 and (City=#{City} or #{City}=-1) order by 2")
    public List<HashMap> getAllByCity(@Param("City") String City);

    @Select("select prov,city,Id,name,phone,email,YYEmail,coordinate,tip,addr,dtPreSale,dtOpen,server,appId,appKey,isnull(nullif(app_signature,''),'小小运动馆')app_signature from TLG_Gym where id=#{id}")
    public Gym findGym(@Param("id") String id);

    @Insert("insert into TLG_City(ProvinceId,CH_Name,EN_Name,Sort,Status)select id,replace(#{city},'市',''),[dbo].[fn_GetPinyin](replace(#{city},'市','')),0,2 from TLG_Province p join (select 1 x)x on p.CH_Name like '${prov}%' left join TLG_City c on c.ProvinceId=p.id and charindex(c.CH_Name,#{city})>0 where c.M_Id is null;" +
            "delete from tlg_gym where Id=#{Id};insert into tlg_gym(Id,CH_Name,prov,city,cityId,phone,fax,email,YYEmail,coordinate,tip,addr,dtPreSale,dtOpen,updateTime,status,server,appId,appKey,app_signature,isPreparing)" +
            "select #{Id},#{CH_Name},#{prov},#{city},M_Id,#{phone},#{fax},#{email},#{YYEmail},#{coordinate},#{tip},#{addr},#{dtPreSale},#{dtOpen},getdate(),#{status},#{server},#{appId},#{appKey},#{app_signature},#{isPreparing} from TLG_City where charindex(CH_Name,#{city})>0")
    public Integer saveGym(Gym gym);

}
