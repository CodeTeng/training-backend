package com.lt.modules.app.model.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @description: Excel实体
 * @author: ~Teng~
 * @date: 2022/11/30 17:08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StuAchievement implements Serializable {

    @ExcelProperty(value = "考试名称", index = 0)
    private String examName;

    @ExcelProperty(value = "逻辑得分", index = 1)
    private Integer logicScore;

    @ExcelProperty(value = "总得分", index = 2)
    private Integer totalScore;

    @ExcelProperty(value = "是否通过", index = 3)
    private String isFlag;
}
