package com.tlgc.controller;

import com.tlgc.Convertor.DataConvert;
import com.tlgc.mapper.LabelMapper;
import com.tlgc.utils.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Machenike on 2019/6/29.
 */
@Slf4j
@RestController
@RequestMapping("/label")
public class LabelCtrl {
    @Autowired
    LabelMapper labelMapper;
    @RequestMapping(value = "/list")
    public Object login(HttpServletRequest request, HttpServletResponse rsp){
        List<HashMap> labels=labelMapper.getLabels();
        return DataConvert.toJson(ResultUtil.success(labels));
    }
    @RequestMapping(value = "/franApp")
    public Object FranAppList(HttpServletRequest request, HttpServletResponse rsp,
                        @RequestParam(value = "FranAppId",required = true) Integer FranAppId
    ){

        List<HashMap> labels=labelMapper.getLabelsForFran(FranAppId);
        return DataConvert.toJson(ResultUtil.success(labels));
    }
    @RequestMapping(value = "/add")
    public Object add(HttpServletRequest request, HttpServletResponse rsp,
                        @RequestParam(value = "name",required = true) String name,
                        @RequestParam(value = "FranAppId",required = true) Integer FranAppId
    ){

        Integer res=labelMapper.labelAdd(name,FranAppId);
        if(res==null) DataConvert.toJson(ResultUtil.error());
        return DataConvert.toJson(ResultUtil.success());
    }
    @RequestMapping(value = "/del")
    public Object del(HttpServletRequest request, HttpServletResponse rsp,
                        @RequestParam(value = "name",required = true) String name,
                        @RequestParam(value = "FranAppId",required = true) Integer FranAppId
    ){

        Integer res=labelMapper.deleteLabelFroFran(name,FranAppId);
        return DataConvert.toJson(ResultUtil.success());
    }
}
