package com.tlgc.controller.API;

import com.fasterxml.jackson.annotation.JsonView;
import com.tlgc.entity.EmployeeNonOMSGym;
import com.tlgc.entity.Result;
import com.tlgc.mapper.lessonplan.AccontMapper;
import com.tlgc.utils.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Tony on 2020/2/7.
 */
@Slf4j
@RestController
@RequestMapping("/lp")
public class LPCtrl {
    @Autowired
    AccontMapper accontMapper;


    @GetMapping("/getAccounts")
    @JsonView(EmployeeNonOMSGym.AccountDetail.class)
    public List<EmployeeNonOMSGym> accounts() {
        List<EmployeeNonOMSGym> employeeNonOMSGyms = accontMapper.getAccounts();
        return employeeNonOMSGyms;
    }
    @GetMapping("/getAccount")
    @JsonView(EmployeeNonOMSGym.AccountDetail.class)
    public EmployeeNonOMSGym account() {
        EmployeeNonOMSGym employeeNonOMSGyms = accontMapper.getAccount("500005");
        return employeeNonOMSGyms;
    }

    @RequestMapping(value = "/saveAccount")
    public Result saveAccount(@RequestParam(value = "callback", required = false) String callback,
                              @RequestParam(value = "gymCode", required = false) String gymCode,
                              @RequestParam(value = "gymName", required = false) String gymName,
                              @RequestParam(value = "city", required = false) String city,
                              @RequestParam(value = "isActive", required = false) Byte isActive)
    {
        Integer res = accontMapper.saveAccount(gymName,gymCode,city,isActive);
        if(res>0) {
            return ResultUtil.success();
        }
        return ResultUtil.error();
    }
}
