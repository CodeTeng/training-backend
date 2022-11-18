package com.lt.modules.sys.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 系统验证码
 *
 * @author teng
 * @TableName captcha
 */
@TableName(value = "captcha")
@Data
public class Captcha implements Serializable {
    /**
     * uuid
     */
    @TableId
    private String uuid;

    /**
     * 验证码
     */
    private String code;

    /**
     * 过期时间
     */
    private Date expireTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}