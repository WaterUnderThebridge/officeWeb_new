package com.tlgc.service.impl;

import com.tlgc.entity.Admin;
import com.tlgc.entity.Result;
import com.tlgc.enums.ResultEnum;
import com.tlgc.mapper.AdminMapper;
import com.tlgc.service.IAdminService;
import com.tlgc.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by hibernate on 2017/5/19.
 */
@Service
public class AdminServiceImpl implements IAdminService {
    @Autowired
    private AdminMapper adminMapper;

    @Override
    public Result login(String username, String password) {

        Admin admin = adminMapper.getAdminByUsername(username);
        if (admin != null && admin.getPassword().equals(password.trim())) {
            return ResultUtil.success(ResultEnum.LOGIN_SUCCESS,admin);
        } else {
            return ResultUtil.error(ResultEnum.LOGIN_WRONG_PWD);
        }

    }
}