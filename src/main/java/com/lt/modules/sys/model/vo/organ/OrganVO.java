package com.lt.modules.sys.model.vo.organ;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @description: 机构视图
 * @author: ~Teng~
 * @date: 2022/11/19 16:22
 */
@Data
public class OrganVO implements Serializable {
    /**
     * 机构id
     */
    private Long id;

    /**
     * 机构名称
     */
    private String name;

    /**
     * 机构类型名称
     */
    private String typeName;

    /**
     * 机构logo图片地址
     */
    private String organLogo;

    /**
     * 机构联系电话
     */
    private String phone;

    /**
     * 机构负责人
     */
    private String chargePerson;

    /**
     * 邮箱
     */
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

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
