package com.lt.modules.sys.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lt.modules.sys.model.dto.question.QuestionRequest;
import com.lt.modules.sys.model.entity.QuestionBank;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lt.modules.sys.model.vo.bank.BankHaveQuestionSumVO;
import com.lt.modules.sys.model.vo.bank.QuestionBankVO;

import java.util.List;

/**
 * @author teng
 * @description 针对表【question_bank(题库表)】的数据库操作Service
 * @createDate 2022-11-22 09:24:12
 */
public interface QuestionBankService extends IService<QuestionBank> {

    Page<QuestionBankVO> getQuestionBank(Integer pageNo, Integer pageSize, String bankName);

    /**
     * 获取题库中所有题目类型的数量
     */
    Page<BankHaveQuestionSumVO> getBankHaveQuestionSumByType(Integer pageNo, Integer pageSize, String bankName, Long organId);

    List<QuestionRequest> getQuestionInfoByBank(Long bankId);
}
