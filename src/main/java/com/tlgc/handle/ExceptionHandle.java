package com.tlgc.handle;

import com.tlgc.entity.Result;
import com.tlgc.exception.MyException;
import com.tlgc.utils.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Tony on 2017/9/13.
 */
@Slf4j
@ControllerAdvice
public class ExceptionHandle {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result myHandle(Exception e){
        if(e instanceof MyException){
            MyException myException= (MyException) e;
            return ResultUtil.error(myException.getCode(),myException.getMessage());
        }else{
            log.error("系统错误：{}",e);
            return ResultUtil.error(-1,"未知错误");
        }
    }

}

