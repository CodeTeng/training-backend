package com.lt.modules.sys.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 视频类别表
 *
 * @TableName video_type
 */
@TableName(value = "video_type")
@Data
public class VideoType implements Serializable {
    /**
     * 视频类别id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 类别名称
     */
    @NotBlank(message = "视频类别名称不能为空")
    private String typeName;

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