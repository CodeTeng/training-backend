package com.lt.modules.sys.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 题库表
 *
 * @author teng
 * @TableName question_bank
 */
@TableName(value = "question_bank")
@Data
public class QuestionBank implements Serializable {
    /**
     * 题库id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 题库名称
     */
    @NotBlank(message = "题库名称不能为空")
    private String bankName;

    /**
     * 机构id
     */
    private Long organId;

    /**
     * 机构名称
     */
    private String organName;

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