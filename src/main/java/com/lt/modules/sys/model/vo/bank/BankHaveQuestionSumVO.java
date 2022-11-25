package com.lt.modules.sys.model.vo.bank;

import lombok.Data;

/**
 * @description: 题库所有信息VO
 * @author: ~Teng~
 * @date: 2022/11/23 12:10
 */
@Data
public class BankHaveQuestionSumVO {
    /**
     * 题库名称
     */
    private String bankName;

    /**
     * 机构名称
     */
    private String organName;

    /**
     * 单选数量
     */
    private Integer singleChoice;

    /**
     * 多选数量
     */
    private Integer multipleChoice;

    /**
     * 判断数量
     */
    private Integer judge;

    /**
     * 简答数量
     */
    private Integer shortAnswer;
}
