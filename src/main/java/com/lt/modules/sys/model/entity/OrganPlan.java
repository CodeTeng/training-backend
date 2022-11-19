package com.lt.modules.sys.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 培训机构计划表
 *
 * @author teng
 * @TableName organ_plan
 */
@TableName(value = "organ_plan")
@Data
public class OrganPlan implements Serializable {
    /**
     * 机构培训计划id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 培训机构id
     */
    @NotBlank(message = "培训机构id不能为空")
    private Long organId;

    /**
     * 培训内容
     */
    @NotBlank(message = "培训内容信息不能为空")
    private String content;

    /**
     * 培训开始时间
     */
    @NotBlank(message = "培训开始时间不能为空")
    private Date startTime;

    /**
     * 培训周期 默认7天
     */
    @NotBlank(message = "培训周期不能为空")
    private Integer trainPeriod;

    /**
     * 培训结束时间 开始时间+培训周期
     */
    private Date endTime;

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