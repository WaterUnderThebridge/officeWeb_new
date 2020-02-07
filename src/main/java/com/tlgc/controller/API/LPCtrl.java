package com.tlgc.controller.API;

import com.tlgc.entity.EmployeeNonOMSGym;
import com.tlgc.mapper.lessonplan.AccontMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Tony on 2020/2/7.
 */
@Slf4j
@RestController
@RequestMapping("/lp")
public class LPCtrl {
    @Autowired
    AccontMapper accontMapper;


    @GetMapping("/getAccount")
    public EmployeeNonOMSGym account() {
        EmployeeNonOMSGym employeeNonOMSGym = accontMapper.getAccount("500005");
        return employeeNonOMSGym;
    }
}
