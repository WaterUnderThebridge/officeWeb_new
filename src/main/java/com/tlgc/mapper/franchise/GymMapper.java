package com.tlgc.mapper.franchise;



import com.tlgc.entity.City;
import com.tlgc.entity.Gym;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by TONY on 2017/11/19.
 */
@Repository
public interface GymMapper {
    @Select("select c.M_Id city_id,c.CH_Name city_name,Id,isPreparing,name cH_Name,isPreparing,name CH_Name,dtPreSale,dtOpen,createTime from TLG_Gym g join tlg_city c on g.cityId=c.M_Id where g.status=1 and g.id not in(${noShow}) and (g.CityId=${CityId} or ${CityId}=-1) order by 2,3")
     List<HashMap> getAllByCityId(@Param("CityId") Integer CityId,@Param("noShow") String noShow);

    @Select("select prov,city,Id,isPreparing,name,phone,email,YYEmail,coordinate,tip,addr,dtPreSale,dtOpen,createTime from TLG_Gym where status=1 and (City=#{City} or #{City}=-1) order by 4,5")
     List<HashMap> getAllByCity(@Param("City") String City);

    @Select("select prov,city,Id,name CH_Name,phone,email,YYEmail,coordinate,tip,addr,dtPreSale,dtOpen,server,appId,appKey,isnull(nullif(app_signature,''),'小小运动馆')app_signature,createTime from TLG_Gym where id=#{id}")
     Gym findGym(@Param("id") String id);

    @Insert("insert into TLG_City(ProvinceId,CH_Name,EN_Name,Sort,Status)select id,replace(#{city},'市',''),[dbo].[fn_GetPinyin](replace(#{city},'市','')),0,2 from TLG_Province p join (select 1 x)x on p.CH_Name like '${prov}%' left join TLG_City c on c.ProvinceId=p.id and charindex(c.CH_Name,#{city})>0 where c.M_Id is null;" +
            "delete from tlg_gym where Id=#{Id};insert into tlg_gym(Id,CH_Name,prov,city,cityId,phone,fax,email,YYEmail,coordinate,tip,addr,dtPreSale,dtOpen,updateTime,status,server,appId,appKey,app_signature,isPreparing,createTime)" +
            "select #{Id},#{CH_Name},#{prov},#{city},M_Id,#{phone},#{fax},#{email},#{YYEmail},#{coordinate},#{tip},#{addr},#{dtPreSale},#{dtOpen},getdate(),#{status},#{server},#{appId},#{appKey},#{app_signature},#{isPreparing},#{createTime} from TLG_City where charindex(CH_Name,#{city})>0")
     Integer saveGym(Gym gym);

}
