package com.tlgc.mapper;

import com.tlgc.entity.Intro;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;

/**
 * Created by TONY on 2017/11/19.
 */
public interface IntroMapper {
    @Transactional
    @Insert("INSERT INTO TLG_FreeAppointment(Center,City,Provnce,BabyName,BabyBrithday,Email,ParentPhone,channel,mailStatus,createTime,Status,Type,search)" +
            "VALUES(#{Center},#{City},#{Province},#{BabyName},#{BabyBrithday},#{Email},#{ParentPhone},#{Channel},#{MailStatus},#{CreateTime},1,1,#{search})")
    public Integer saveIntro(Intro intro);

    public List<HashMap> getIntro(@Param("gymCode")String gymCode,@Param("dtBegin")String dtBegin,@Param("dtEnd")String dtEnd,@Param("keyWord")String keyWord);
}
