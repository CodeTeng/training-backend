package com.lt.modules.sys.model.dto.question;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2022/11/23 19:26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionRequest {
    /**
     * 题目id
     */
    private Long id;

    /**
     * 题目类型
     */
    private Integer type;

    /**
     * 题目难度等级
     */
    private Integer level;

    /**
     * 所属题库id集合
     */
    private Long[] bankIds;

    /**
     * 题目内容
     */
    private String content;

    /**
     * 题目图片URL 可以为多张
     */
    private String[] contentImages;

    /**
     * 题目解析
     */
    private String analysis;

    private String creator;

    private String updater;

    /**
     * 答案数组
     */
    private Answer[] answers;

    /**
     * 答案对象
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Answer {
        private Long id;

        /**
         * 是否是答案
         */
        private String isTrue;

        /**
         * 答案内容
         */
        private String answer;

        /**
         * 答案图片
         */
        private String[] images;

        /**
         * 答案解析
         */
        private String analysis;
    }
}
