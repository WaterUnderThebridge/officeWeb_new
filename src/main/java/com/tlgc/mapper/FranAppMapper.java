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
    @Select("select count(1) from TLG_AffiliateInfo where isdelete=0 and mailstatus=0 and Phone=#{Phone} and datediff(d,#{dtApp},createTime)=0")
    public Integer findApp(@Param("Phone") String Phone,@Param("dtApp") String dtApp);


    public Integer updateFranApp(@Param("id") Integer id,@Param("name") String name,@Param("phone") String phone,@Param("email") String email,@Param("channel") String channel,@Param("address") String address,@Param("dt") String dt,@Param("nextTime") String nextTime,@Param("linkTime") Integer linkTime,@Param("status") Integer status,@Param("wechatName") String wechatName);

    public Integer updateFranApps(@Param("ids") String[] ids,@Param("FollowerID") Integer FollowerID);

    public List<HashMap> listFranApp(@Param("FollowerID") Integer FollowerID,@Param("dtBegin") String dtBegin,@Param("dtEnd") String dtEnd,@Param("keyWord") String keyWord ,@Param("pageSize") Integer pageSize,@Param("pageNum") Integer pageNum,@Param("sort") String sort,@Param("todayFollow") String todayFollow,@Param("advSearch") String advSearch,@Param("advSearch2") String advSearch2,@Param("unAllocate") String unAllocate);

    @Insert("INSERT INTO TLG_AffiliateInfo (Name,Phone,wechatName,Email,Address,Channel,MailStatus,Remark,LinkTime,CreateTime,status,rec_name,rec_phone,search,followerId)" +
            "VALUES(#{Name},#{Phone},#{wechatName},#{Email},#{Address},#{Channel},#{MailStatus},#{Remark},#{LinkTime},#{CreateTime},1,#{rec_name},#{rec_phone},#{Search},#{followerId})")
    public Integer saveFranApp(FranApp franApp);

    public Integer deleteFranApp(@Param("ids") String[] ids);
    @Insert("${sql}")
    public Integer saveFranApps(@Param("sql") String sql);

    @Select("select channel from [dbo].[TLG_AffiliateInfo] where channel<>'' group by channel")
    public List<HashMap> listFranAppChannel();



}
