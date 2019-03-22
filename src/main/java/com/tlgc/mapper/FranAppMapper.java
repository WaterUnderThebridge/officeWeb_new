package com.tlgc.mapper;

import com.tlgc.entity.FranApp;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.HashMap;
import java.util.List;

/**
 * Created by TONY on 2018/4/11.
 */
public interface FranAppMapper {
    @Select("select count(1) from TLG_AffiliateInfo where mailstatus=0 and Phone=#{Phone} and datediff(d,#{dtApp},createTime)=0")
    public Integer findApp(@Param("Phone") String Phone,@Param("dtApp") String dtApp);

    public List<HashMap> listFranApp(@Param("dtBegin") String dtBegin,@Param("dtEnd") String dtEnd,@Param("keyWord") String keyWord);

    @Insert("INSERT INTO TLG_AffiliateInfo (Name,Phone,Email,Address,Channel,MailStatus,Remark,LinkTime,CreateTime,status,rec_name,rec_phone)" +
            "VALUES(#{Name},#{Phone},#{Email},#{Address},#{Channel},#{MailStatus},#{Remark},#{LinkTime},#{CreateTime},1,#{rec_name},#{rec_phone})")
    public Integer saveFranApp(FranApp franApp);

    public Integer handleApp(@Param("ids")String[] ids);


}
