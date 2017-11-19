package com.tlgc.mapper;

import com.tlgc.entity.Admin;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

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
}
