package com.tlgc.controller.API;

import com.tlgc.Convertor.DataConvert;



import com.tlgc.entity.GoTong;

import com.tlgc.mapper.franchise.GotongMapper;
import com.tlgc.utils.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by TONY on 2019/6/27.
 */
@Slf4j
@RestController
@RequestMapping("/gt")
public class GoTongCtrl {
    @Autowired
    private GotongMapper gotongMapper;
    @RequestMapping(value = "/save")
    public Object save(HttpServletRequest request, HttpServletResponse rsp,
                        @RequestParam(value = "content",defaultValue = "") String content,
                        @RequestParam(value = "dtGotong",defaultValue = "") Date dtGotong,
                        @RequestParam(value = "FranAppId",required = true) Integer FranAppId,
                        @RequestParam(value = "userId",required = true) Integer userId
    ){

        GoTong goTong=new GoTong();
        goTong.setContent(content);
        goTong.setDtGotong(dtGotong);
        goTong.setFranAppId(FranAppId);
        goTong.setUserId(userId);
        System.out.print(goTong.getFranAppId());
        Integer res=gotongMapper.saveGoTong(goTong);
        if(res!=null)  return ResultUtil.success();
        return ResultUtil.error();

    }

    @RequestMapping(value = "/list")
    public Object list(HttpServletRequest request, HttpServletResponse rsp,
                       @RequestParam(value = "FranAppId",required = true) Integer FraAppId
    ){


         List<HashMap> goTongs=gotongMapper.getGotongs(FraAppId);
        if(goTongs!=null)  return ResultUtil.success(goTongs);
        return ResultUtil.error();
    }

    @RequestMapping(value = "/delete")
    public Object delete(HttpServletRequest request, HttpServletResponse rsp,
                       @RequestParam(value = "id",defaultValue = "") Integer id
    ){

        gotongMapper.deleteById(id);
        return ResultUtil.success();
    }
}
