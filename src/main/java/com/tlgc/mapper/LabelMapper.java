package com.tlgc.mapper;

import org.apache.ibatis.annotations.*;

import java.util.HashMap;
import java.util.List;

/**
 * Created by TONY on 2017/10/3.
 */
public interface LabelMapper {

    @Select("select id,name value from TLG_Labels")
    @Results({
            @Result(property="id",column="id"),
            @Result(property="value",column="value"),
    })
    public List<HashMap> getLabels ();

    @Insert("insert into TLG_Labels(name)select top 1 t.name from(select #{name} name)t left join TLG_Labels l on l.name=t.name where l.name is null;\n" +
            "insert into TLG_LabelForFraApp (labelId,FranAppId)select top 1 id,#{FranAppId} from TLG_Labels where name=#{name}")
    public Integer labelAdd(@Param("name") String name, @Param("FranAppId") Integer FranAppId);


    @Select("select distinct l.name from TLG_LabelForFraApp f join TLG_Labels l on f.FranAppId=#{FranAppId} and f.labelId=l.id")
    @Results({
            @Result(property="name",column="name"),
    })
    public List<HashMap> getLabelsForFran (@Param("FranAppId") Integer FranAppId);

    @Delete("delete f from TLG_LabelForFraApp f join TLG_Labels l on l.name=#{name} and f.FranAppId=#{FranAppId} and l.id=f.labelId;\n" +
            "delete l from TLG_Labels l where l.name=#{name} and not exists(select 1 from TLG_LabelForFraApp f where l.id=f.labelId)")
    public Integer deleteLabelFroFran(@Param("name") String name,@Param("FranAppId") Integer FranAppId);
}
