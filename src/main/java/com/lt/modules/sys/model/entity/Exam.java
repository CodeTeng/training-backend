package com.lt.modules.sys.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 考试表
 *
 * @author teng
 * @TableName exam
 */
@TableName(value = "exam")
@Data
public class Exam implements Serializable {
    /**
     * 考试id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 机构id
     */
    private Long organId;

    /**
     * 机构名称
     */
    private String organName;

    /**
     * 考试名称
     */
    private String examName;

    /**
     * 考试介绍
     */
    private String examDesc;

    /**
     * 考试开始时间
     */
    private Date startTime;

    /**
     * 持续时间-单位为秒
     */
    private Integer totalTime;

    /**
     * 考试结束时间
     */
    private Date endTime;

    /**
     * 考试总分
     */
    private Integer totalScore;

    /**
     * 考试通过线
     */
    private Integer passScore;

    /**
     * 考试有效 0-有效 1-无效
     */
    private Integer valid;

    /**
     * 学员须知
     */
    private String tips;

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