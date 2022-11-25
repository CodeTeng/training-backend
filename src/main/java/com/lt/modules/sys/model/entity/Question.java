package com.lt.modules.sys.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 题目表
 *
 * @author teng
 * @TableName question
 */
@TableName(value = "question")
@Data
public class Question implements Serializable {
    /**
     * 题目id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 题目内容
     */
    private String content;

    /**
     * 题目类型 1单选 2多选 3判断 4简答
     */
    private Integer type;

    /**
     * 题目难度 1简单 2中等 3困难
     */
    private Integer level;

    /**
     * 题目内容图片 可能为看图答题 可以为多张
     */
    private String contentImage;

    /**
     * 所属题库id 可以属于多个题目 eg:1,2
     */
    private String bankId;

    /**
     * 所属题库名称 多个以,分隔
     */
    private String bankName;

    /**
     * 整个题目解析
     */
    private String analysis;

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