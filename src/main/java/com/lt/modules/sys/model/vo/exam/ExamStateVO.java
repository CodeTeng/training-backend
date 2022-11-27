package com.lt.modules.sys.model.vo.exam;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @description: 考试记录VO
 * @author: ~Teng~
 * @date: 2022/11/27 23:32
 */
@Data
public class ExamStateVO implements Serializable {
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
     * 更新者
     */
    private String updater;

    /**
     * 是否考过 true表示考过  false表示没有考过
     */
    private Boolean flag;
}
