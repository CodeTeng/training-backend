package com.lt.modules.app.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @description: 学员考试成绩VO
 * @author: ~Teng~
 * @date: 2022/11/30 16:21
 */
@Data
public class StuExamRecordVO implements Serializable {
    /**
     * 考试记录id
     */
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
     * 考试名称
     */
    private String examName;

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
     * 是否考过 true表示考过  false表示没有考过
     */
    private Boolean flag;
}
