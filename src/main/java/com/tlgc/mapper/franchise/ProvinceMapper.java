package com.tlgc.mapper.franchise;


import com.tlgc.entity.Province;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by TONY on 2017/11/18.
 */
@Repository
public interface ProvinceMapper {
    @Select("select Id,CH_Name from TLG_Province where status=2 order by CH_Name")
    @Results({
            @Result(id=true,property="id",column="id"),
            @Result(property="CH_Name",column="CH_Name"),
    })
     List<Province> getAll();

    @Select("select Id,CH_Name from TLG_Province where Id=${Id}")
      Province getOne(@Param("Id") Integer Id);
}
