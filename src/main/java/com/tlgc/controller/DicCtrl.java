package com.tlgc.controller;

import com.tlgc.Convertor.DataConvert;
import com.tlgc.mapper.DicMapper;
import com.tlgc.utils.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Machenike on 2019/7/5.
 */
@Slf4j
@RestController
@RequestMapping("/dic")

public class DicCtrl {
    @Autowired
    DicMapper dicMapper;
    @RequestMapping(value = "/channel/list")
    public Object channelList(@RequestParam(value="content",defaultValue = "")String content){
        List<HashMap> channelList=dicMapper.getChannelList(content);
        return DataConvert.toJson(ResultUtil.success(channelList));
    }

    @RequestMapping(value = "/channel/add")
    public Object channelAdd(@RequestParam(value="name",required =true)String name){
        Integer res=dicMapper.channelAdd(name);
        if(res==null)  return DataConvert.toJson(ResultUtil.error());
        return DataConvert.toJson(ResultUtil.success());
    }
    @RequestMapping(value = "/channel/del")
    public Object channelAdd(@RequestParam(value="id",required =true)Integer id){
        Integer res=dicMapper.channelDel(id);
        if(res==null)  return DataConvert.toJson(ResultUtil.error());
        return DataConvert.toJson(ResultUtil.success());
    }

    @RequestMapping(value = "/channel/update")
    public Object channelAdd(@RequestParam(value="id",required =true)Integer id,@RequestParam(value="name",required =true)String name){
        Integer res=dicMapper.channelUpdate(id,name);
        if(res==null)  return DataConvert.toJson(ResultUtil.error());
        return DataConvert.toJson(ResultUtil.success());
    }
}
