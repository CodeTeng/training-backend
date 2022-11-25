package com.lt.modules.sys.model.dto.bank;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @description: 添加题库请求
 * @author: ~Teng~
 * @date: 2022/11/23 12:47
 */
@Data
public class AddBankRequest {
    /**
     * 题库名称
     */
    @NotBlank(message = "题库名称不能为空")
    private String bankName;

    /**
     * 机构名称
     */
    @NotBlank(message = "机构名称不能为空")
    private String organName;
}
