package com.tlgc.mapper.franchise;


import com.tlgc.entity.Role;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

/**
 * Created by TONY on 2017/10/3.
 */
@Repository
public interface RoleMapper {
    @Select("select * from tlg_roles where id=#{id}")
     Role getRoleById(@Param("id") Integer id);
    @Select("select * from tlg_roles where id>1" )
     List<HashMap>  getRoleList();
}
