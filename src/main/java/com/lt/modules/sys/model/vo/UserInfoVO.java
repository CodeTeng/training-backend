package com.lt.modules.sys.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2022/11/23 15:46
 */
@Data
public class UserInfoVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    private Long id;

    /**
     * 用户账号
     */
    private String username;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户角色名称
     */
    private String roleName;

    /**
     * 用户所属机构
     */
    private String organName;

    /**
     * 头像地址
     */
    private String avatar;

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
     * 账号状态 0-正常 1-停用 2-审核
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;
}
