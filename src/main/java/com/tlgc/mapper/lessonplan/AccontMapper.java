package com.tlgc.mapper.lessonplan;

import com.tlgc.entity.EmployeeNonOMSGym;
import org.apache.ibatis.annotations.Param;

/**
 * Created by Tony on 2020/2/7.
 */
public interface   AccontMapper {
    EmployeeNonOMSGym getAccount(@Param("gymCode") String gymCode);
}
