package com.tlgc.mapper;

import com.tlgc.entity.Role;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * Created by TONY on 2017/10/3.
 */
public interface RoleMapper {
    @Select("select * from tlg_roles where id=#{id}")
    public Role getRoleById(@Param("id") Integer id);
}
