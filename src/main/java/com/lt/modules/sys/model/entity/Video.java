package com.lt.modules.sys.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 视频表
 *
 * @author teng
 * @TableName video
 */
@TableName(value = "video")
@Data
public class Video implements Serializable {
    /**
     * 视频id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 培训计划id
     */
    private Long organPlanId;

    /**
     * 视频类别id
     */
    private Long videoTypeId;

    /**
     * 视频标题
     */
    @NotBlank(message = "视频标题不能为空")
    private String videoTitle;

    /**
     * 视频封面地址
     */
    private String coverUrl;

    /**
     * 视频地址
     */
    @NotBlank(message = "视频地址不能为空")
    private String videoUrl;

    /**
     * 视频状态 0-正常 1-停用 2-审核
     */
    private Integer status;

    /**
     * 是否发布 0-发布 1-未发布
     */
    private Integer isPublish;

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