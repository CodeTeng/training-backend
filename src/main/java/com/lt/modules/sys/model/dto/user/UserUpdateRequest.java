package com.lt.modules.sys.model.dto.user;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

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

    /**
     * 机构id
     */
    private Long organId;

    /**
     * 角色id
     */
    private List<Long> roleIdList;

    /**
     * 用户账号
     */
    private String username;

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
     * 头像地址
     */
    private String avatar;

    /**
     * 账号状态 0-正常 1-停用 2-审核
     */
    private Integer status;

    /**
     * 更新者
     */
    private String updater;
}
