package com.lt.modules.app.service;

import com.lt.modules.app.entity.dto.UserRegisterRequest;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2022/11/20 20:54
 */
public interface StuService {
    /**
     * 用户注册
     */
    long userRegister(UserRegisterRequest userRegisterRequest);
}
