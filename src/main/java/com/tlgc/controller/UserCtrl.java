package com.tlgc.controller;

import com.tlgc.Convertor.DataConvert;
import com.tlgc.entity.FranApp;
import com.tlgc.entity.User;
import com.tlgc.enums.ResultEnum;
import com.tlgc.mapper.UserMapper;
import com.tlgc.utils.ResultUtil;
import com.tlgc.utils.TokenTools;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Created by TONY on 2019/6/20.
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserCtrl {
    @Autowired
    private UserMapper userMapper;

    @RequestMapping(value = "/login")
    public Object login(HttpServletRequest rqs,HttpServletResponse rsp,
                            @RequestParam(value = "username",defaultValue = "") String username,
                            @RequestParam(value = "password",defaultValue = "") String password
    ){
        rsp.addHeader("Access-Control-Allow-Origin", "*");
        rsp.setHeader("Content-Type", "application/json;charset=UTF-8");
        User user=userMapper.getUserByUsername(username);
        password=DigestUtils.md5Hex(password);
        String token=TokenTools.createToken(rqs,username);

        if (user==null) {
            return DataConvert.toJson(ResultUtil.error(ResultEnum.LOGIN_USER_NO_EXIST));
        }else {
            user.setToken(token);
            if (password.equals(user.getPassword())) {
                return DataConvert.toJson(ResultUtil.success(user));
            }
        }
        return DataConvert.toJson(ResultUtil.error(ResultEnum.LOGIN_WRONG_PWD));

    }
    @RequestMapping(value = "/addUser")
    public Object addUser(){
        return DataConvert.toJson(ResultUtil.error(ResultEnum.LOGIN_WRONG_PWD));
    }
    @RequestMapping(value = "/delUser")
    public Object delUser(){
        return DataConvert.toJson(ResultUtil.error(ResultEnum.LOGIN_WRONG_PWD));
    }
    @RequestMapping(value = "/logut")
    public Object logut(HttpServletRequest rqs,@RequestParam(value = "username",defaultValue = "") String username){
        TokenTools.removeToken(rqs,username);
        return DataConvert.toJson(ResultUtil.error(ResultEnum.LOGIN_WRONG_PWD));
    }

    @RequestMapping(value = "/timeout")
    public Object timeout(){
        return DataConvert.toJson(ResultUtil.error(ResultEnum.LOGIN_WRONG_PWD));
    }

}
