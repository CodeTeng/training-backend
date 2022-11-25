package com.lt.modules.sys.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lt.modules.sys.model.dto.question.QuestionRequest;
import com.lt.modules.sys.model.entity.Question;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author teng
 * @description 针对表【question(题目表)】的数据库操作Service
 * @createDate 2022-11-22 09:24:12
 */
public interface QuestionService extends IService<Question> {

    Page<Question> getQuestion(Integer pageNo, Integer pageSize, Integer questionType, Integer questionBankId, String questionContent);

    void addQuestion(QuestionRequest questionRequest);

    void updateQuestion(QuestionRequest questionRequest);

    QuestionRequest getQuestionVOById(Long questionId);

    boolean addQuestionToBank(String questionIds, String bankIds);

    boolean removeQuestionFromBank(String questionIds, String bankIds);
}
