package com.lt.modules.sys.service;

import com.lt.modules.sys.model.entity.Captcha;
import com.baomidou.mybatisplus.extension.service.IService;

import java.awt.image.BufferedImage;

/**
 * @author teng
 * @description 针对表【captcha(系统验证码)】的数据库操作Service
 * @createDate 2022-11-17 12:01:10
 */
public interface CaptchaService extends IService<Captcha> {
    /**
     * 获取图片验证码
     */
    BufferedImage getCaptcha(String uuid);

    /**
     * 验证码效验
     *
     * @param uuid uuid
     * @param code 验证码
     * @return true：成功  false：失败
     */
    boolean validate(String uuid, String code);
}
