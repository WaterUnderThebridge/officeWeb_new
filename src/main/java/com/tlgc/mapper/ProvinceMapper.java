package com.tlgc.mapper;

import com.tlgc.entity.Province;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by TONY on 2017/11/18.
 */
public interface ProvinceMapper {
    @Select("select Id,CH_Name from TLG_Province where status=2 order by CH_Name")
    @Results({
            @Result(id=true,property="id",column="id"),
            @Result(property="CH_Name",column="CH_Name"),
    })
    public List<Province> getAll();

    @Select("select Id,CH_Name from TLG_Province where Id=${Id}")
    public  Province getOne(@Param("Id") Integer Id);
}
