package com.lt.modules.sys.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户视图
 *
 * @author teng
 * @TableName user
 */
@Data
public class UserVO implements Serializable {
    /**
     * id
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
     * 用户头像
     */
    private String avatar;

    /**
     * 性别
     */
    private Integer sex;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}