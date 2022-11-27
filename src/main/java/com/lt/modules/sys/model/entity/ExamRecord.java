package com.lt.modules.sys.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 考试记录表
 *
 * @author teng
 * @TableName exam_record
 */
@TableName(value = "exam_record")
@Data
public class ExamRecord implements Serializable {
    /**
     * 考试记录id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 学员id
     */
    private Long userId;

    /**
     * 用户的答案列表 多个以-分隔 不能用,有多选题
     */
    private String userAnswers;

    /**
     * 考试诚信截图
     */
    private String creditImgUrl;

    /**
     * 考试id
     */
    private Long examId;

    /**
     * 考试的逻辑得分(除简答)
     */
    private Integer logicScore;

    /**
     * 考生作答时间
     */
    private Date examTime;

    /**
     * 考试题目id列表 多个以,分隔
     */
    private String questionIds;

    /**
     * 考试总分(逻辑 + 简答)
     */
    private Integer totalScore;

    /**
     * 用户考试的错题id列表
     */
    private String errorQuestionIds;

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