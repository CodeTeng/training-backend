package com.lt.modules.sys.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 学习情况表
 *
 * @author teng
 * @TableName learn_condition
 */
@TableName(value = "learn_condition")
@Data
public class LearnCondition implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 学员id
     */
    private Long userId;

    /**
     * 视频id
     */
    private Long videoId;

    /**
     * 是否完成 0-未完成 1-完成
     */
    private Integer isDone;

    /**
     * 完成时间
     */
    private Date doneTime;

    /**
     * 视频观看完成度
     */
    private Integer complete;

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