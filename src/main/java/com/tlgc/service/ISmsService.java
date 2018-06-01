package com.tlgc.service;

import com.tlgc.entity.PhoneMsg;

import java.util.Map;

/**
 * Created by hibernate on 2018/5/24.
 */
public interface ISmsService {
    public Map<String, Object> synchPhoneMsg(PhoneMsg phoneMsg);

}
