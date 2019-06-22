package com.tlgc.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by TONY on 2019/6/20.
 */

@Slf4j
public class TokenTools {
    /**
     * 生成token放入session
     * @param request
     * @param tokenServerkey
     */
    public static String createToken(HttpServletRequest request, String tokenServerkey){

        String token_client = TokenProccessor.getInstance().makeToken();
        request.getSession().setAttribute(tokenServerkey, token_client);
        return token_client;
    }

    /**
     * 移除token
     * @param request
     * @param tokenServerkey
     */
    public static void removeToken(HttpServletRequest request,String tokenServerkey){
        request.getSession().removeAttribute(tokenServerkey);
    }

    /**
     * 判断请求参数中的token是否和session中一致
     * @param request
     * @param tokenClientkey
     * @param tokenServerkey
     * @return
     */
    public static boolean judgeTokenIsEqual(HttpServletRequest request,String tokenClientkey,String tokenServerkey){
        String token_client = request.getParameter("token");
        log.error(token_client);
        if(StringUtils.isEmpty(token_client)){
            return false;
        }
        String token_server = (String) request.getSession().getAttribute("token");
        log.error(token_server);
        if(StringUtils.isEmpty(token_server)){
            return false;
        }
        if(!token_server.equals(token_client)){
            return false;
        }

        return true;
    }
}
