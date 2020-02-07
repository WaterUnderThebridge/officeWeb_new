package com.tlgc.mapper.franchise;


import com.tlgc.entity.Admin;
import com.tlgc.entity.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;

/**
 * Created by TONY on 2017/10/2.
 */
@Repository
public interface DicMapper {

    @Insert("INSERT INTO tlg_channel(name,isdelete)" +
            "VALUES(#{name},0)")
    public Integer channelAdd(@Param("name") String name);

    @Update("update tlg_channel set isdelete=1 where id=#{id}")
    public Integer channelDel(@Param("id") Integer id);

    @Update("update TLG_AffiliateInfo set channel=#{name} where channel in(select name from tlg_channel where id=${id});update tlg_channel set name=#{name} where id=${id}")
    public Integer channelUpdate(@Param("id") Integer id,@Param("name") String name);

    @Select("select name,id,isdelete  from tlg_channel where name like '%${channel}%' order by createTime desc")
    public List<HashMap>  getChannelList(@Param("channel") String channel);


}
