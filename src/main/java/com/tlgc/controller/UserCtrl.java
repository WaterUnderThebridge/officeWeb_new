package com.tlgc.controller;

import com.tlgc.Convertor.DataConvert;
import com.tlgc.WebSecurityConfig;
import com.tlgc.entity.FranApp;
import com.tlgc.entity.User;
import com.tlgc.enums.ResultEnum;
import com.tlgc.mapper.RoleMapper;
import com.tlgc.mapper.UserMapper;
import com.tlgc.utils.ResultUtil;
import com.tlgc.utils.TokenTools;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by TONY on 2019/6/20.
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserCtrl {
    @Autowired
    private UserMapper userMapper;
    private RoleMapper roleMapper;

    @RequestMapping(value = "/login")
    public Object login(HttpServletRequest request, HttpServletResponse rsp,
                        @RequestParam(value = "username",defaultValue = "") String username,
                        @RequestParam(value = "password",defaultValue = "") String password
    ){
        rsp.addHeader("Access-Control-Allow-Origin", "*");
        rsp.setHeader("Content-Type", "application/json;charset=UTF-8");
        User user=userMapper.getUserByUsername(username);

        HttpSession sessoin=request.getSession();//这就是session的创建
        String token=TokenTools.createToken(request,"token");

        password=DigestUtils.md5Hex(password);
        //log.error(token);
        if (user==null) {
            return DataConvert.toJson(ResultUtil.error(ResultEnum.LOGIN_USER_NO_EXIST));
        }else {
            user.setToken(token);
            //request.getSession().setAttribute(username,user);
            if (password.equals(user.getPassword())) {
                return DataConvert.toJson(ResultUtil.success(user));
            }
        }

        return DataConvert.toJson(ResultUtil.error(ResultEnum.LOGIN_WRONG_PWD));

    }

    @RequestMapping(value = "/info")
    public Object info(HttpServletRequest rqs,HttpServletResponse rsp,@RequestParam(value = "username",defaultValue = "") String username,
                       @RequestParam(value = "token",defaultValue = "") String token){
        rsp.addHeader("Access-Control-Allow-Origin", "*");
        rsp.setHeader("Content-Type", "application/json;charset=UTF-8");

        User user=userMapper.getUserByUsername(username);
        return DataConvert.toJson(ResultUtil.success(user));
    }

    @RequestMapping(value = "/userList")
    public Object geUsers(HttpServletRequest rqs,HttpServletResponse rsp){
        rsp.addHeader("Access-Control-Allow-Origin", "*");
        rsp.setHeader("Content-Type", "application/json;charset=UTF-8");

        List<HashMap> users=userMapper.getUserList();
        return DataConvert.toJson(ResultUtil.success(users));
    }
    @RequestMapping(value = "/roleList")
    public Object getRoles(HttpServletRequest rqs,HttpServletResponse rsp){
        rsp.addHeader("Access-Control-Allow-Origin", "*");
        rsp.setHeader("Content-Type", "application/json;charset=UTF-8");

        List<HashMap> roles=roleMapper.getRoles();
        return DataConvert.toJson(ResultUtil.success(roles));
    }

    @RequestMapping(value = "/userDel")
    public Object delUser(@RequestParam(value = "id",required = true) Integer id){
        Integer res=userMapper.userDel(id);
        if(res==null) return DataConvert.toJson(ResultUtil.error());
        return DataConvert.toJson(ResultUtil.error(ResultEnum.SUCCESS));
    }
    @RequestMapping(value = "/userAdd")
    public Object addUser(@RequestParam(value = "username",defaultValue = "") String username,@RequestParam(value = "fullname",defaultValue = "") String fullname,
                          @RequestParam(value = "password",defaultValue = "") String password,@RequestParam(value = "roleId",defaultValue = "") Integer roleId){
        password=DigestUtils.md5Hex(password);
        Integer res=userMapper.userAdd(username,fullname,password,roleId);
        if(res==null) return DataConvert.toJson(ResultUtil.error());
        return DataConvert.toJson(ResultUtil.error(ResultEnum.LOGIN_WRONG_PWD));
    }
    @RequestMapping(value = "/userUpdate")
    public Object updateUser(@RequestParam(value = "id",required = true) Integer id,@RequestParam(value = "username",defaultValue = "") String username,@RequestParam(value = "password",defaultValue = "") String password,
                             @RequestParam(value = "fullname",defaultValue = "") String fullname,@RequestParam(value = "roleId",defaultValue = "") Integer roleId) {
        password = DigestUtils.md5Hex(password);
        Integer res=userMapper.userUpdate(id,username,fullname,password,roleId);
        return DataConvert.toJson(ResultUtil.error(ResultEnum.LOGIN_WRONG_PWD));
    }
    @RequestMapping(value = "/logout")
    public Object logut(HttpServletRequest rqs,HttpServletResponse rsp,@RequestParam(value = "username",defaultValue = "") String username){
        rsp.addHeader("Access-Control-Allow-Origin", "*");
        rsp.setHeader("Content-Type", "application/json;charset=UTF-8");
        TokenTools.removeToken(rqs,"token");
        return DataConvert.toJson(ResultUtil.error(ResultEnum.LOGIN_LOGOUT));
    }
    @RequestMapping(value = "/resetToken")
    public Object resetToken(HttpServletRequest rqs,HttpServletResponse rsp,@RequestParam(value = "username",defaultValue = "") String username){
        rsp.addHeader("Access-Control-Allow-Origin", "*");
        rsp.setHeader("Content-Type", "application/json;charset=UTF-8");
        TokenTools.removeToken(rqs,"token");
        return DataConvert.toJson(ResultUtil.error(ResultEnum.SUCCESS));
    }

    @RequestMapping(value = "/timeout")
    public Object timeout(HttpServletResponse rsp){
        rsp.addHeader("Access-Control-Allow-Origin", "*");
        rsp.setHeader("Content-Type", "application/json;charset=UTF-8");
        return DataConvert.toJson(ResultUtil.error(ResultEnum.TIMEOUT));
    }

}
