package com.lt.modules.sys.model.dto.exam;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @description: 添加试卷请求体（根据题目列表添加）
 * @author: ~Teng~
 * @date: 2022/11/24 15:02
 */
@Data
public class AddExamByQuestionListRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 考试id
     */
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
     * 考试通过线
     */
    private Integer passScore;

    /**
     * 考试总分
     */
    private Integer totalScore;

    /**
     * 学员须知
     */
    private String tips;

    /**
     * 所选题目id 多个以,分隔
     */
    private String questionIds;

    /**
     * 所选每道题分数 多个以,分隔
     */
    private String scores;

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
}
