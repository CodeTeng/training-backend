package com.lt.modules.sys.model.dto.user;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * @description: 密码表单
 * @author: ~Teng~
 * @date: 2022/11/17 21:28
 */
@Data
public class UserPasswordRequest {
    /**
     * 原密码
     */
    @NotBlank(message = "密码不能为空")
    @Min(value = 6, message = "密码最短为6位")
    private String password;

    /**
     * 新密码
     */
    @NotBlank(message = "密码不能为空")
    @Min(value = 6, message = "密码最短为6位")
    private String newPassword;
}
