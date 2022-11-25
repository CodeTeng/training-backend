package com.lt.modules.sys.model.vo.bank;

import lombok.Data;

/**
 * @description: 题库信息VO
 * @author: ~Teng~
 * @date: 2022/11/22 10:53
 */
@Data
public class QuestionBankVO {
    /**
     * 题库名称
     */
    private String bankName;

    /**
     * 所属机构名称
     */
    private String organName;
}
