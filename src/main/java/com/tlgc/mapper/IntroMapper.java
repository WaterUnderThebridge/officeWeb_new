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
    @Insert("INSERT INTO TLG_FreeAppointment(Center,City,Provnce,BabyName,BabyBrithday,Email,ParentPhone,channel,mailStatus,createTime,Status,Type)" +
            "VALUES(#{Center},#{City},#{Province},#{BabyName},#{BabyBrithday},#{Email},#{ParentPhone},#{Channel},#{MailStatus},#{CreateTime},1,1)")
    public Integer saveIntro(Intro intro);

    public List<HashMap> getIntro(String gymCode,String dtBegin,String dtEnd);
}
