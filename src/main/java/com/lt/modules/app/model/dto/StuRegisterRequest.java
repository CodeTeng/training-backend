package com.lt.modules.app.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @description: 用户注册请求体
 * @author: ~Teng~
 * @date: 2022/11/20 20:43
 */
@Data
public class StuRegisterRequest implements Serializable {
    /**
     * 用户账号
     */
    private String username;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 确认密码
     */
    private String checkPassword;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 用户性别 0-男 1-女
     */
    private Integer sex;

    /**
     * 创建者
     */
    private String creator;

    private static final long serialVersionUID = 1L;
}
