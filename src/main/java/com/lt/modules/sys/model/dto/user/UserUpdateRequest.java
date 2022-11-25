package com.lt.modules.sys.model.dto.user;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @description: 修改用户请求体
 * @author: ~Teng~
 * @date: 2022/11/23 17:44
 */
@Data
public class UserUpdateRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    private Long id;

    private String organName;

    /**
     * 多个的话 以,分隔
     */
    @NotBlank(message = "角色名称不能为空")
    private String roleName;

    /**
     * 用户账号
     */
    @NotBlank(message = "用户名不能为空")
    private String username;

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
    @NotBlank(message = "用户性别不能为空")
    private String sexName;

    /**
     * 账号状态 0-正常 1-停用 2-审核
     */
    @NotBlank(message = "账号状态不能为空")
    private String statusName;

    /**
     * 更新者
     */
    private String updater;
}
