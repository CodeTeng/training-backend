package com.lt.modules.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.modules.sys.model.entity.UserToken;
import com.lt.modules.sys.oauth2.TokenGenerator;
import com.lt.modules.sys.service.UserTokenService;
import com.lt.modules.sys.mapper.UserTokenMapper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author teng
 * @description 针对表【user_token(系统用户Token)】的数据库操作Service实现
 * @createDate 2022-11-17 11:26:32
 */
@Service
public class UserTokenServiceImpl extends ServiceImpl<UserTokenMapper, UserToken>
        implements UserTokenService {

    /**
     * 12h 后过期
     */
    private final static int EXPIRE = 3600 * 12;

    @Override
    public Map<String, Object> createToken(Long userId) {
        // 生成一个 token
        String token = TokenGenerator.generateValue();
        // 当前时间
        Date now = new Date();
        // 过期时间
        Date expireTime = new Date(now.getTime() + EXPIRE * 1000);
        // 判断是否生成过 token
        UserToken userToken = this.getById(userId);
        if (userToken == null) {
            userToken = new UserToken();
            userToken.setUserId(userId);
            userToken.setToken(token);
            userToken.setUpdateTime(now);
            userToken.setExpireTime(expireTime);
            // 保存
            this.save(userToken);
        } else {
            userToken.setToken(token);
            userToken.setUpdateTime(now);
            userToken.setExpireTime(expireTime);
            // 更新
            this.updateById(userToken);
        }
        Map<String, Object> map = new HashMap<>(2);
        map.put("token", token);
        map.put("expire", EXPIRE);
        return map;
    }

    @Override
    public void logout(Long userId) {
        // 生成一个token
        String token = TokenGenerator.generateValue();
        // 修改token
        UserToken userToken = new UserToken();
        userToken.setUserId(userId);
        userToken.setToken(token);
        this.updateById(userToken);
    }
}




