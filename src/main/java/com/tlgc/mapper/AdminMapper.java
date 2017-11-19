package com.tlgc.mapper;

import com.tlgc.entity.Admin;
import org.apache.ibatis.annotations.*;

/**
 * Created by TONY on 2017/10/3.
 */
public interface AdminMapper {

    @Select("select * from admin where username=#{username} and isDelete=0 limit 1")
    @Results({
            @Result(id=true,property="id",column="id"),
            @Result(property="username",column="username"),
            @Result(property="password",column="password"),
            @Result(property="role",column="roleId",one=@One(select="com.tlgc.mapper.RoleMapper.getRoleById"))
    })
    public  Admin getAdminByUsername(String username);
}
