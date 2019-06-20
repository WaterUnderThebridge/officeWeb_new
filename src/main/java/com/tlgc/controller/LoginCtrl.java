package com.tlgc.controller;

import com.tlgc.Convertor.DataConvert;
import com.tlgc.entity.FranApp;
import com.tlgc.entity.User;
import com.tlgc.enums.ResultEnum;
import com.tlgc.mapper.UserMapper;
import com.tlgc.utils.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Created by TONY on 2019/6/20.
 */
@Slf4j
@RestController
@RequestMapping("/login")
public class LoginCtrl {
    @Autowired
    private UserMapper userMapper;

    @RequestMapping(value = "/login")
    public Object saveAppli(HttpServletResponse rsp,
                            @RequestParam(value = "username",defaultValue = "") String username,
                            @RequestParam(value = "password",defaultValue = "") String Phone
    ){
        rsp.addHeader("Access-Control-Allow-Origin", "*");
        rsp.setHeader("Content-Type", "application/json;charset=UTF-8");
        User user=userMapper.getUserByUsername(username);

        System.out.print(user);
        if (Phone.equals("")) {
            return DataConvert.toJson(ResultUtil.error("手机号必须填写"));
        }

        return DataConvert.toJson(ResultUtil.error(ResultEnum.LOGIN_WRONG_PWD));

    }

}
