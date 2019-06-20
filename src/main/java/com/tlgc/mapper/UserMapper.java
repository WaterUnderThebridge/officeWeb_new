package com.tlgc.mapper;

import com.tlgc.entity.Admin;
import com.tlgc.entity.User;
import org.apache.ibatis.annotations.*;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by TONY on 2017/10/2.
 */
public interface UserMapper {
    public List<Admin> getAll();
    public Admin getOne(Long id);

    @Transactional
    @Update("update tlg_user set city='${city}' where id=${userId}")
    public Integer updateCity(@Param("userId") Integer userId, @Param("city") String city);

    @Select("select id,username,password,roleId from tlg_user where username=#{username} and isDelete=0 ")
    @Results({
            @Result(id=true,property="id",column="id"),
            @Result(property="username",column="username"),
            @Result(property="password",column="password"),
            @Result(property="role",column="roleId",one=@One(select="com.tlgc.mapper.RoleMapper.getRoleById"))
    })
    public User getUserByUsername(String username);
}
