package com.tlgc.mapper;

import com.tlgc.entity.Admin;
import com.tlgc.entity.GoTong;
import org.apache.ibatis.annotations.*;

import java.util.HashMap;
import java.util.List;

/**
 * Created by TONY on 2017/10/3.
 */
public interface GotongMapper {

    @Select("select g.id,isnull(convert(varchar(10),dtGotong,120)+','+u.fullname+'：','')title,content  from TLG_GoTong g join TLG_User u on g.FranAppId=#{FranAppId} and u.id=g.userid order by dtGotong desc,g.id desc")
    @Results({
            @Result(id=true,property="id",column="id"),
            @Result(property="content",column="content")
    })
    public List<HashMap> getGotongs(Integer FranAppId);
    @Delete("delete from TLG_GoTong where id=#{Id}")
    public void  deleteById(Integer Id);

    @Insert("insert into [dbo].[TLG_Gotong](FranAppId,content,dtGotong,userid,createTime,isdelete)" +
            "values(#{FranAppId},#{content},#{dtGotong},#{userid},#{createTime},#{isdelete})")
    public Integer saveGoTong(GoTong goTong);
}
