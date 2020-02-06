package com.tlgc;


import com.tlgc.Convertor.DataConvert;
import com.tlgc.entity.Result;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.AbstractJsonpResponseBodyAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 全局返回值统一封装
 */

@Configuration
public class GlobalReturnConfig {

    @RestControllerAdvice(basePackages={"com.tlgc.controller.API"})
    public class Jsonp extends AbstractJsonpResponseBodyAdvice {
        public Jsonp() {
            //构造函数
            super("callback","jsonp");
        }

    }

    @RestControllerAdvice(basePackages={"com.tlgc.controller.API"})
    static class ResultResponseAdvice implements ResponseBodyAdvice<Object> {
        @Override
        public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
            return true;
        }

        @Override
        public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
            if(body instanceof Result) {
                Result result = (Result) body;
                //method2: HttpServletRequest request = ((ServletServerHttpRequest) serverHttpRequest).getServletRequest();
                //Map<String, String> params = DataConvert.URLRequest(serverHttpRequest.getURI().getQuery());
                //String callback = params.get("callback");
                return DataConvert.toJson(result);
            }
            return body;
        }
    }
}