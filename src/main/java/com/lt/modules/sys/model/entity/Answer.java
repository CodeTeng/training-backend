package com.lt.modules.sys.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 答案表
 *
 * @author teng
 * @TableName answer
 */
@TableName(value = "answer")
@Data
public class Answer implements Serializable {
    /**
     * 答案id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 所有答案内容 eg:测试选项Alist_separator测试选型Blist_separator测试选型Clist_separator测试选型D 4个选型
     */
    private String allOption;

    /**
     * 所有答案的图片路径 以,分隔
     */
    private String imagesUrl;

    /**
     * 所有答案的解析 以list_separator分隔
     */
    private String analysis;

    /**
     * 对应题目的id
     */
    private Long questionId;

    /**
     * 正确的选项对应的下标
     */
    private String trueOption;

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