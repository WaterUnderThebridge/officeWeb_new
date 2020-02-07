package com.tlgc.mapper.lessonplan;

import com.tlgc.entity.EmployeeNonOMSGym;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Tony on 2020/2/7.
 */
public interface  AccontMapper {
    EmployeeNonOMSGym getAccount(@Param("gymCode") String gymCode);

    List<EmployeeNonOMSGym> getAccounts();

    Integer saveAccount(@Param("gymName") String gymName,@Param("gymCode") String gymCode,@Param("city") String city,@Param("isActive") Byte isActive);


}
