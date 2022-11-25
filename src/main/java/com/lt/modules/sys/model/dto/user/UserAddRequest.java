package com.lt.modules.sys.model.dto.user;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @description: 添加用户请求
 * @author: ~Teng~
 * @date: 2022/11/23 17:06
 */
@Data
public class UserAddRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "用户名不能为空")
    private String username;

    private String organName;

    /**
     * 多个的话 以,分隔
     */
    @NotBlank(message = "角色名称不能为空")
    private String roleName;

    @NotBlank(message = "密码不能为空")
    private String password;

    @NotBlank(message = "昵称不能为空")
    private String nickname;

    @Email(message = "必须为邮箱格式")
    private String email;

    @NotBlank(message = "手机号码不能为空")
    private String mobile;

    @NotBlank(message = "性别不能为空")
    private String sexName;

    private String creator;
}
