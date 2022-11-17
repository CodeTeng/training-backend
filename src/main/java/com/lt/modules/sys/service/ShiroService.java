package com.lt.modules.sys.service;

import com.lt.modules.sys.model.entity.User;
import com.lt.modules.sys.model.entity.UserToken;

import java.util.Set;

/**
 * @description: shiro相关接口
 * @author: ~Teng~
 * @date: 2022/11/17 11:22
 */
public interface ShiroService {
    /**
     * 获取用户权限列表
     */
    Set<String> getUserPermissions(long userId);

    UserToken queryByToken(String token);

    /**
     * 根据用户ID，查询用户
     *
     * @param userId 用户id
     */
    User queryUser(Long userId);
}
