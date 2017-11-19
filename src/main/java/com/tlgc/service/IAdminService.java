package com.tlgc.service;

import com.tlgc.entity.Result;


/**
 * Created by hibernate on 2017/5/19.
 */
public interface IAdminService {
    //登录
    public Result login(String username, String password) ;
}
