package com.lt.modules.sys.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 考试题目关联表
 *
 * @author teng
 * @TableName exam_question
 */
@TableName(value = "exam_question")
@Data
public class ExamQuestion implements Serializable {
    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 考试的题目id列表 以,分隔
     */
    private String questionIds;

    /**
     * 考试id
     */
    private Long examId;

    /**
     * 每一道题的分数 以,分隔
     */
    private String scores;

    /**
     * 创建时间
     */
    private Date createTime;

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