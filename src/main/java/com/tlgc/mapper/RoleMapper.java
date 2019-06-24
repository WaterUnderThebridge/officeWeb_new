package com.tlgc.mapper;

import com.tlgc.entity.Role;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.HashMap;
import java.util.List;

/**
 * Created by TONY on 2017/10/3.
 */
public interface RoleMapper {
    @Select("select * from tlg_roles where id=#{id}")
    public Role getRoleById(@Param("id") Integer id);
    @Select("select * from tlg_roles")
    public List<HashMap> getRoles();
}
