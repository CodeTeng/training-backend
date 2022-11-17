package com.lt.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.code.kaptcha.Producer;
import com.lt.common.ErrorCode;
import com.lt.common.utils.DateUtils;
import com.lt.common.exception.BusinessException;
import com.lt.modules.sys.model.entity.Captcha;
import com.lt.modules.sys.service.CaptchaService;
import com.lt.modules.sys.mapper.CaptchaMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.util.Date;

/**
 * @author teng
 * @description 针对表【captcha(系统验证码)】的数据库操作Service实现
 * @createDate 2022-11-17 12:01:10
 */
@Service
public class CaptchaServiceImpl extends ServiceImpl<CaptchaMapper, Captcha>
        implements CaptchaService {

    @Autowired
    private Producer producer;

    @Override
    public BufferedImage getCaptcha(String uuid) {
        if (StringUtils.isBlank(uuid)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "uuid不能为空");
        }
        // 生成文字验证码
        String code = producer.createText();
        Captcha captchaEntity = new Captcha();
        captchaEntity.setUuid(uuid);
        captchaEntity.setCode(code);
        // 5分钟后过期
        captchaEntity.setExpireTime(DateUtils.addDateMinutes(new Date(), 5));
        this.save(captchaEntity);
        return producer.createImage(code);
    }

    @Override
    public boolean validate(String uuid, String code) {
        Captcha captchaEntity = this.getOne(new QueryWrapper<Captcha>().eq("uuid", uuid));
        if (captchaEntity == null) {
            return false;
        }
        //删除验证码
        this.removeById(uuid);
        if (captchaEntity.getCode().equalsIgnoreCase(code) && captchaEntity.getExpireTime().getTime() >= System.currentTimeMillis()) {
            return true;
        }
        return false;
    }
}




