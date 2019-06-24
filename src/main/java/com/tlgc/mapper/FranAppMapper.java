package com.tlgc.mapper;

import com.tlgc.entity.FranApp;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by TONY on 2018/4/11.
 */
public interface FranAppMapper {
    @Select("select count(1) from TLG_AffiliateInfo where mailstatus=0 and Phone=#{Phone} and datediff(d,#{dtApp},createTime)=0")
    public Integer findApp(@Param("Phone") String Phone,@Param("dtApp") String dtApp);

    public Integer deleteFranApp(@Param("ids") String[] ids);

    public Integer updateFranApp(@Param("id") Integer id,@Param("nextTime") String nextTime,@Param("status") Integer status);

    public Integer updateFranApps(@Param("ids") String[] ids,@Param("FollowerID") Integer FollowerID);

    public List<HashMap> listFranApp(@Param("FollowerID") Integer FollowerID,@Param("dtBegin") String dtBegin,@Param("dtEnd") String dtEnd,@Param("keyWord") String keyWord ,@Param("pageSize") Integer pageSize,@Param("pageNum") Integer pageNum,@Param("sort") String sort,@Param("todayFollow") String todayFollow);

    @Insert("INSERT INTO TLG_AffiliateInfo (Name,Phone,Email,Address,Channel,MailStatus,Remark,LinkTime,CreateTime,status,rec_name,rec_phone,search)" +
            "VALUES(#{Name},#{Phone},#{Email},#{Address},#{Channel},#{MailStatus},#{Remark},#{LinkTime},#{CreateTime},1,#{rec_name},#{rec_phone},#{Search})")
    public Integer saveFranApp(FranApp franApp);

}
