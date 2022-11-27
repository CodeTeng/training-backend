package com.lt.modules.sys.model.dto.examRecord;

import lombok.Data;

import java.io.Serializable;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2022/11/26 19:08
 */
@Data
public class AddExamRecordRequest implements Serializable {
    /**
     * 用户的答案列表
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
     * 考试题目id列表
     */
    private String questionIds;
}
