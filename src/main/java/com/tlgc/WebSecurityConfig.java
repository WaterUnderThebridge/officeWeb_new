package com.tlgc;

/**
 * Created by Machenike on 2019/6/20.
 */
import com.tlgc.Convertor.DataConvert;
import com.tlgc.enums.ResultEnum;
import com.tlgc.utils.ResultUtil;
import com.tlgc.utils.TokenTools;
import lombok.extern.slf4j.Slf4j;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;



@Configuration
@Slf4j
public class WebSecurityConfig extends WebMvcConfigurerAdapter {
    public final static String SESSION_KEY = "token";
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/web/**").addResourceLocations("classpath:/web/");
        super.addResourceHandlers(registry);
    }

    /**
     * 登录session key
     */
    @Bean
    public SecurityInterceptor getSecurityInterceptor() {
        return new SecurityInterceptor();
    }

    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration addInterceptor = registry.addInterceptor(getSecurityInterceptor());

        // 排除配置
        addInterceptor.excludePathPatterns("/user/login**");
        addInterceptor.excludePathPatterns("/user/logout**");
        addInterceptor.excludePathPatterns("/user/timeout**");

        //拦截配置
        addInterceptor.addPathPatterns("/user/**");

    }

    private class SecurityInterceptor extends HandlerInterceptorAdapter {
        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            log.info("..............intercepted................."+request.getRequestURI());

            String token = request.getParameter("token");

            if(token==null||token.equals("")){
                ResultUtil.writeJson(response,DataConvert.toJson(ResultUtil.error(ResultEnum.LOGIN_NO_ACCESS_RIGHT)));
                return false;
            }
            return true;
        }
    }
}