package com.lt.modules.oss.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2022/11/19 10:39
 */
@Component
public class ConstantProperties implements InitializingBean {

    @Value("${qcloud.oss.file.region}")
    private String region;
    @Value("${qcloud.oss.file.secretId}")
    private String secretId;
    @Value("${qcloud.oss.file.secretKey}")
    private String secretKey;
    @Value("${qcloud.oss.file.bucketName}")
    private String bucketName;

    /**
     * 定义公开的静态变量
     */
    public static String REGION;

    public static String SECRET_ID;
    public static String SECRET_KEY;
    public static String BUCKET_NAME;

    @Override
    public void afterPropertiesSet() throws Exception {
        REGION = region;
        SECRET_ID = secretId;
        SECRET_KEY = secretKey;
        BUCKET_NAME = bucketName;
    }
}
