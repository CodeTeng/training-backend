package com.lt.modules.sys.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 菜单表
 *
 * @TableName menu
 */
@TableName(value = "menu")
@Data
public class Menu implements Serializable, Comparable<Menu> {
    /**
     * 菜单id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 菜单名称
     */
    @NotBlank(message = "菜单名称不能为空")
    private String name;

    /**
     * 权限标识(多个用逗号分隔，如：user:list,user:create)
     */
    private String permission;

    /**
     * 菜单类型 0-目录 1-菜单 2-按钮
     */
    @NotBlank(message = "菜单类型不能为空")
    private Integer type;

    /**
     * 父菜单id 1级菜单id为0
     */
    @NotBlank(message = "父菜单id不能为空")
    private Long parentId;

    /**
     * 父菜单名称
     */
    @TableField(exist = false)
    private String parentName;

    /**
     * 菜单接口url
     */
    private String url;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 排序
     */
    @NotBlank(message = "排序字段不能为空")
    private Integer orderNum;

    /**
     * ztree属性
     */
    @TableField(exist = false)
    private Boolean open;

    @TableField(exist = false)
    private List<Menu> list = new ArrayList<>();

    @Override
    public int compareTo(Menu o) {
        return this.getOrderNum() - o.getOrderNum();
    }

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