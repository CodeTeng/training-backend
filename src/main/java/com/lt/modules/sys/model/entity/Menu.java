package com.lt.modules.sys.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Data;

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
    private String name;

    /**
     * 权限标识(多个用逗号分隔，如：user:list,user:create)
     */
    private String permission;

    /**
     * 菜单类型 0-目录 1-菜单 2-按钮
     */
    private Integer type;

    /**
     * 父菜单id 1级菜单id为0
     */
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
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}