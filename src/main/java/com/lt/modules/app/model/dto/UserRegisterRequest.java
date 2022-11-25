package com.lt.modules.app.model.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @description: 用户注册请求体
 * @author: ~Teng~
 * @date: 2022/11/20 20:43
 */
@Data
public class UserRegisterRequest implements Serializable {
    /**
     * 用户账号
     */
    @NotBlank(message = "用户名不能为空")
    private String username;

    /**
     * 用户密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;

    @NotBlank(message = "确认密码不能为空")
    private String checkPassword;

    /**
     * 用户昵称
     */
    @NotBlank(message = "用户昵称不能为空")
    private String nickname;

    /**
     * 用户邮箱
     */
    @Email(message = "邮箱格式不正确")
    private String email;

    /**
     * 手机号码
     */
    @NotBlank(message = "手机号不能为空")
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
