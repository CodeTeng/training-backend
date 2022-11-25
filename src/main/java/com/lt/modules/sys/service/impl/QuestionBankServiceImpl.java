package com.lt.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.constant.ExamConstant;
import com.lt.modules.sys.model.dto.question.QuestionRequest;
import com.lt.modules.sys.model.entity.Answer;
import com.lt.modules.sys.model.entity.Question;
import com.lt.modules.sys.model.entity.QuestionBank;
import com.lt.modules.sys.model.vo.bank.BankHaveQuestionSumVO;
import com.lt.modules.sys.model.vo.bank.QuestionBankVO;
import com.lt.modules.sys.service.AnswerService;
import com.lt.modules.sys.service.QuestionBankService;
import com.lt.modules.sys.mapper.QuestionBankMapper;
import com.lt.modules.sys.service.QuestionService;
import com.lt.modules.sys.service.OrganService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author teng
 * @description 针对表【question_bank(题库表)】的数据库操作Service实现
 * @createDate 2022-11-22 09:24:12
 */
@Service
public class QuestionBankServiceImpl extends ServiceImpl<QuestionBankMapper, QuestionBank>
        implements QuestionBankService {

    @Autowired
    private QuestionBankMapper questionBankMapper;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private AnswerService answerService;

    @Override
    public Page<QuestionBankVO> getQuestionBank(Integer pageNo, Integer pageSize, String bankName) {
        QueryWrapper<QuestionBank> bankQueryWrapper = new QueryWrapper<>();
        bankQueryWrapper.like(StringUtils.isNotBlank(bankName), "bankName", bankName);
        Page<QuestionBank> questionBankPage = this.page(
                new Page<>(pageNo, pageSize),
                bankQueryWrapper
        );
        List<QuestionBankVO> questionBankVOList = questionBankMapper.getQuestionBank(bankName);
        Page<QuestionBankVO> resPage = new Page<>();
        BeanUtils.copyProperties(questionBankPage, resPage);
        resPage.setRecords(questionBankVOList);
        return resPage;
    }

    @Override
    public Page<BankHaveQuestionSumVO> getBankHaveQuestionSumByType(Integer pageNo, Integer pageSize, String bankName, Long organId) {
        QueryWrapper<QuestionBank> bankQueryWrapper = new QueryWrapper<>();
        bankQueryWrapper.eq(organId != null && organId > 0, "organId", organId);
        bankQueryWrapper.like(StringUtils.isNotBlank(bankName), "bankName", bankName);
        Page<QuestionBank> questionBankPage = this.page(
                new Page<>(pageNo, pageSize),
                bankQueryWrapper
        );
        Page<BankHaveQuestionSumVO> resPage = new Page<>();
        BeanUtils.copyProperties(questionBankPage, resPage);
        List<QuestionBank> questionBankList = questionBankPage.getRecords();
        List<BankHaveQuestionSumVO> bankHaveQuestionSumVOList = new ArrayList<>();
        for (QuestionBank questionBank : questionBankList) {
            BankHaveQuestionSumVO questionSumVO = new BankHaveQuestionSumVO();
            BeanUtils.copyProperties(questionBank, questionSumVO);
            Long bankId = questionBank.getId();
            // 单选题数量
            List<Question> singleQuestions = questionService.list(
                    new QueryWrapper<Question>().eq("type", 1).like("bankId", bankId));
            questionSumVO.setSingleChoice(singleQuestions.size());
            // 多选题数量
            List<Question> multipleQuestions = questionService.list(
                    new QueryWrapper<Question>().eq("type", 2).like("bankId", bankId));
            questionSumVO.setMultipleChoice(multipleQuestions.size());
            // 判断题数量
            List<Question> judgeQuestions = questionService.list(
                    new QueryWrapper<Question>().eq("type", 3).like("bankId", bankId));
            questionSumVO.setJudge(judgeQuestions.size());
            // 简单题数量
            List<Question> shortAnswerQuestions = questionService.list(
                    new QueryWrapper<Question>().eq("type", 4).like("bankId", bankId));
            questionSumVO.setShortAnswer(shortAnswerQuestions.size());
            bankHaveQuestionSumVOList.add(questionSumVO);
        }
        resPage.setRecords(bankHaveQuestionSumVOList);
        return resPage;
    }

    @Override
    public List<QuestionRequest> getQuestionInfoByBank(Long bankId) {
        QuestionBank questionBank = this.getById(bankId);
        List<Question> questionList = questionService.list(new QueryWrapper<Question>().like("bankId", questionBank.getId()).in("type", 1, 2, 3));
        // 构造前端需要的vo对象
        List<QuestionRequest> questionRequests = new ArrayList<>();
        for (Question question : questionList) {
            if (question.getType() == 4) {
                continue;
            }
            QuestionRequest questionRequest = new QuestionRequest();
            BeanUtils.copyProperties(question, questionRequest);
            String strBankId = question.getBankId();
            String[] strBankIds = StringUtils.split(strBankId, ",");
            Long[] bankIds = new Long[strBankIds.length];
            for (int i = 0; i < strBankIds.length; i++) {
                bankIds[i] = Long.parseLong(strBankIds[i]);
            }
            // 设置题库集合
            questionRequest.setBankIds(bankIds);
            String contentImage = question.getContentImage();
            if (StringUtils.isNotBlank(contentImage)) {
                String[] contentImages = StringUtils.splitByWholeSeparator(contentImage, ",");
                // 设置图片集合
                questionRequest.setContentImages(contentImages);
            }
            Answer answer = answerService.getOne(new QueryWrapper<Answer>().eq("questionId", question.getId()));
            // 答案内容
            String[] allOptions = StringUtils.splitByWholeSeparator(answer.getAllOption(), ExamConstant.SEPARATOR);
            // 答案解析
            String[] analysis = StringUtils.splitByWholeSeparator(answer.getAnalysis(), ExamConstant.SEPARATOR);
            // 答案图片
            String[] images = StringUtils.splitByWholeSeparator(answer.getImagesUrl(), ",");
            // 构造答案对象
            QuestionRequest.Answer[] handleAnswer = new QuestionRequest.Answer[analysis.length];
            for (int i = 0; i < allOptions.length; i++) {
                QuestionRequest.Answer answer1 = new QuestionRequest.Answer();
                if (images != null) {
                    if (images.length - 1 >= i && StringUtils.isNotBlank(images[i])) {
                        answer1.setImages(new String[]{images[i]});
                    }
                }
                answer1.setAnswer(allOptions[i]);
                answer1.setAnalysis(analysis[i]);
                answer1.setId((long) i);
                answer1.setIsTrue("false");
                handleAnswer[i] = answer1;
            }
            if (question.getType() == 1 || question.getType() == 3) {
                // 单选和判断
                int trueOption = Integer.parseInt(answer.getTrueOption());
                handleAnswer[trueOption].setIsTrue("true");
            } else if (question.getType() == 2) {
                // 多选
                String[] trueOptions = StringUtils.splitByWholeSeparator(answer.getTrueOption(), ",");
                for (String trueOption : trueOptions) {
                    handleAnswer[Integer.parseInt(trueOption)].setIsTrue("true");
                }
            }
            questionRequest.setAnswers(handleAnswer);
            questionRequests.add(questionRequest);
        }
        return questionRequests;
    }
}




