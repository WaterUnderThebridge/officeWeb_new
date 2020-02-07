package com.tlgc.mapper.franchise;


import com.tlgc.entity.Admin;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

/**
 * Created by TONY on 2017/10/3.
 */
@Repository
public interface AdminMapper {
    @Select("select * from admin where username=#{username} and isDelete=0 limit 1")
    @Results({
            @Result(id=true,property="id",column="id"),
            @Result(property="username",column="username"),
            @Result(property="password",column="password"),
            @Result(property="role",column="roleId",one=@One(select="com.tlgc.mapper.franchise.RoleMapper.getRoleById"))
    })
    Admin getAdminByUsername(String username);
}
