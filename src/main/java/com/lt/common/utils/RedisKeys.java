package com.lt.common.utils;

/**
 * @description: Redis所有Keys
 * @author: ~Teng~
 * @date: 2022/11/18 21:42
 */
public class RedisKeys {
    public static String getSysConfigKey(String key) {
        return "sys:config:" + key;
    }
}
