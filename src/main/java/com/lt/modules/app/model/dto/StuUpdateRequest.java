package com.lt.modules.app.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @description: 前台用户更新请求
 * @author: ~Teng~
 * @date: 2022/11/29 22:25
 */
@Data
public class StuUpdateRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    private Long id;

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
     * 更新者
     */
    private String updater;
}
