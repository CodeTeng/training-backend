package com.lt.modules.sys.model.dto.user;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @description: 密码表单
 * @author: ~Teng~
 * @date: 2022/11/17 21:28
 */
@Data
public class UserPasswordRequest implements Serializable {
    /**
     * 原密码
     */
    private String password;

    /**
     * 新密码
     */
    private String newPassword;
}
