package com.lt.modules.sys.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * 系统用户表
 *
 * @author teng
 * @TableName user
 */
@TableName(value = "user")
@Data
public class User implements Serializable {
    /**
     * 用户id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 所属机构id
     */
    private Long organId;

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
     * 角色ID列表
     */
    @TableField(exist = false)
    private List<Long> roleIdList;

    /**
     * 创建者
     */
    private String creator;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新者
     */
    private String updater;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除 0-不删除 1-删除
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}