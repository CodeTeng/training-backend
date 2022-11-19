package com.lt.modules.sys.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * 培训机构表
 *
 * @author teng
 * @TableName organ
 */
@TableName(value = "organ")
@Data
public class Organ implements Serializable {
    /**
     * 机构id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 机构名称
     */
    @NotBlank(message = "机构名称不能为空")
    private String name;

    /**
     * 机构类型id
     */
    @NotBlank(message = "机构类型不能为空")
    private Long organTypeId;

    /**
     * 机构logo图片地址
     */
    private String organLogo;

    /**
     * 机构联系电话
     */
    @NotBlank(message = "联系电话不能为空")
    private String phone;

    /**
     * 机构负责人
     */
    @NotBlank(message = "负责人不能为空")
    private String chargePerson;

    /**
     * 邮箱
     */
    @Email(message = "不符合邮箱格式")
    private String email;

    /**
     * 机构简介
     */
    private String intro;

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