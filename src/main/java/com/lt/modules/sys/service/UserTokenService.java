package com.lt.modules.sys.service;

import com.lt.modules.sys.model.entity.UserToken;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * @author teng
 * @description 针对表【user_token(系统用户Token)】的数据库操作Service
 * @createDate 2022-11-17 11:26:32
 */
public interface UserTokenService extends IService<UserToken> {

    /**
     * 生成token
     *
     * @param userId 用户id
     */
    Map<String, Object> createToken(Long userId);

    /**
     * 退出，修改token值
     *
     * @param userId 用户ID
     */
    void logout(Long userId);
}
