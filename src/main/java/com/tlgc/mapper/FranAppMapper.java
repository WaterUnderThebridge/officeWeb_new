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
    @Select("select count(1) from TLG_AffiliateInfo where mailstatus=0 and Phone=#{Phone}")
    public Integer findApp(@Param("Phone") String Phone);
    @Select("SELECT  Id,Name,Phone,Email,Status,CreateTime FROM dbo.TLG_AffiliateInfo where CreateTime>=#{dtBegin} and CreateTime<#{dtEnd} " +
            "and charindex(Status,#{keyWord})>0 and charindex(#{keyWord},Search)>0")
    public List<HashMap> listFranApp(@Param("dtBegin") String dtBegin,@Param("dtEnd") String dtEnd,@Param("keyWord") String keyWord,@Param("Status") String Status);

    @Insert("INSERT INTO TLG_AffiliateInfo (Name,Phone,Email,Address,MailStatus)" +
            "VALUES(#{Name},#{Phone},#{Email},#{Address},#{MailStatus})")
    public Integer saveFranApp(FranApp franApp);


}
