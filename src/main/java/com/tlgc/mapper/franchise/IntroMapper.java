package com.tlgc.mapper.franchise;


import com.tlgc.entity.Intro;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;


import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;

/**
 * Created by TONY on 2017/11/19.
 */
@Repository
public interface IntroMapper {
    @Transactional
    @Insert("INSERT INTO TLG_FreeAppointment(gymCode,Center,City,Provnce,BabyName,BabyBrithday,Email,ParentPhone,channel,mailStatus,createTime,Status,Type,search,isSync)" +
            "VALUES(#{gymCode},#{Center},#{City},#{Province},#{BabyName},#{BabyBrithday},#{Email},#{ParentPhone},#{Channel},#{MailStatus},#{CreateTime},1,1,#{search},#{isSync})")
     Integer saveIntro(Intro intro);

     List<HashMap> getIntro(@Param("gymCode")String gymCode,@Param("dtBegin")String dtBegin,@Param("dtEnd")String dtEnd,@Param("keyWord")String keyWord);

     Integer handleIntro(@Param("ids")String[] ids);

    @Select("select count(1) from TLG_FreeAppointment where mailstatus=0 and ParentPhone=#{ParentPhone} and gymCode=#{gymCode}")
     Integer findIntro(@Param("ParentPhone") String ParentPhone,@Param("gymCode") String gymCode);


}
