package com.lt.modules.sys.reids;

import com.lt.common.utils.RedisKeys;
import com.lt.common.utils.RedisUtils;
import com.lt.modules.sys.model.entity.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @description: 系统配置Redis
 * @author: ~Teng~
 * @date: 2022/11/18 21:40
 */
@Component
public class ConfigRedis {
    @Autowired
    private RedisUtils redisUtils;

    public void saveOrUpdate(Config config) {
        if (config == null) {
            return;
        }
        String key = RedisKeys.getSysConfigKey(config.getParamKey());
        redisUtils.set(key, config);
    }

    public void delete(String configKey) {
        String key = RedisKeys.getSysConfigKey(configKey);
        redisUtils.delete(key);
    }

    public Config get(String configKey) {
        String key = RedisKeys.getSysConfigKey(configKey);
        return redisUtils.get(key, Config.class);
    }
}
