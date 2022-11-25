package com.lt.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.constant.ExamConstant;
import com.lt.modules.sys.model.dto.question.QuestionRequest;
import com.lt.modules.sys.model.entity.Answer;
import com.lt.modules.sys.model.entity.Question;
import com.lt.modules.sys.model.entity.QuestionBank;
import com.lt.modules.sys.service.AnswerService;
import com.lt.modules.sys.service.QuestionBankService;
import com.lt.modules.sys.service.QuestionService;
import com.lt.modules.sys.mapper.QuestionMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author teng
 * @description 针对表【question(题目表)】的数据库操作Service实现
 * @createDate 2022-11-22 09:24:12
 */
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question>
        implements QuestionService {

    @Autowired
    private QuestionBankService questionBankService;

    @Autowired
    private AnswerService answerService;

    @Autowired
    private QuestionService questionService;

    @Override
    public Page<Question> getQuestion(Integer pageNo, Integer pageSize, Integer questionType, Integer questionBankId, String questionContent) {
        QueryWrapper<Question> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(questionType != null, "type", questionType);
        queryWrapper.eq(questionBankId != null, "bankId", questionBankId);
        queryWrapper.like(StringUtils.isNotBlank(questionContent), "content", questionContent);
        Page<Question> page = this.page(
                new Page<>(pageNo, pageSize),
                queryWrapper
        );
        return page;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addQuestion(QuestionRequest questionRequest) {
        // 先添加题目
        Question question = new Question();
        BeanUtils.copyProperties(questionRequest, question);
        // 设置所属题库id
        Long[] bankIds = questionRequest.getBankIds();
        String bankId = StringUtils.join(bankIds, ",");
        question.setBankId(bankId);
        // 设置题库名称
        List<String> bankNameList = new ArrayList<>();
        for (Long id : bankIds) {
            QuestionBank questionBank = questionBankService.getById(id);
            String bankName = questionBank.getBankName();
            bankNameList.add(bankName);
        }
        String bankName = StringUtils.join(bankNameList, ",");
        question.setBankName(bankName);
        // 设置题目封面
        String[] contentImages = questionRequest.getContentImages();
        String contentImage = StringUtils.join(contentImages, ",");
        question.setContentImage(contentImage);
        // 保存题目
        this.save(question);
        // 设置答案对象
        Integer type = questionRequest.getType();
        if (type != 4) {
            // 不为简答题 由于简答题没有固定答案 所以不设置答案
            Answer answer = new Answer();
            answer.setQuestionId(question.getId());
            answer.setCreator(question.getCreator());
            List<String> imageList = new ArrayList<>();
            List<String> answerList = new ArrayList<>();
            List<Integer> multiChoice = new ArrayList<>();
            List<String> analysisList = new ArrayList<>();
            for (int i = 0; i < questionRequest.getAnswers().length; i++) {
                for (int j = 0; j < questionRequest.getAnswers()[i].getImages().length; j++) {
                    // 设置答案图片信息
                    imageList.add(questionRequest.getAnswers()[i].getImages()[j]);
                }
                // 设置答案内容
                answerList.add(questionRequest.getAnswers()[i].getAnswer());
                // 设置答案解析
                analysisList.add(questionRequest.getAnswers()[i].getAnalysis());
                // 设置对应的选型的下标
                if (type == 2) {
                    // 多选题
                    if ("true".equalsIgnoreCase(questionRequest.getAnswers()[i].getIsTrue())) {
                        // 是答案 添加下标
                        multiChoice.add(i);
                    }
                } else {
                    // 单选和判断只有一个答案
                    if ("true".equalsIgnoreCase(questionRequest.getAnswers()[i].getIsTrue())) {
                        answer.setTrueOption(i + "");
                    }
                }
            }
            if (type == 2) {
                String trueOption = StringUtils.join(multiChoice, ",");
                answer.setTrueOption(trueOption);
            }
            if (imageList.size() > 0) {
                String imageUrl = StringUtils.join(imageList, ",");
                answer.setImagesUrl(imageUrl);
            }
            if (answerList.size() > 0) {
                String allOption = StringUtils.join(answerList, ExamConstant.SEPARATOR);
                answer.setAllOption(allOption);
            }
            if (analysisList.size() > 0) {
                String analysis = StringUtils.join(analysisList, ExamConstant.SEPARATOR);
                answer.setAnalysis(analysis);
            }
            // 保存答案
            answerService.save(answer);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateQuestion(QuestionRequest questionRequest) {
        Question question = new Question();
        BeanUtils.copyProperties(questionRequest, question);
        // 设置题库id
        Long[] bankIds = questionRequest.getBankIds();
        List<String> bankIdList = new ArrayList<>();
        List<String> bankNameList = new ArrayList<>();
        for (Long bankId : bankIds) {
            bankIdList.add(String.valueOf(bankId));
            QuestionBank questionBank = questionBankService.getById(bankId);
            bankNameList.add(questionBank.getBankName());
        }
        String bankId = StringUtils.join(bankIdList, ",");
        question.setBankId(bankId);
        // 设置题库名称
        String bankName = StringUtils.join(bankNameList, ",");
        question.setBankName(bankName);
        // 设置题目图片
        String[] contentImages = questionRequest.getContentImages();
        if (contentImages != null && contentImages.length > 0) {
            List<String> imageList = new ArrayList<>();
            for (String contentImage : contentImages) {
                imageList.add(contentImage);
            }
            String contentImage = StringUtils.join(imageList, ",");
            question.setContentImage(contentImage);
        }
        // 更新题目信息
        questionService.updateById(question);
        // 设置答案对象
        Integer type = questionRequest.getType();
        if (type != 4) {
            // 不为简单题
            Answer answer = new Answer();
            answer.setUpdater(questionRequest.getUpdater());
            answer.setQuestionId(question.getId());
            List<String> allOptionList = new ArrayList<>();
            List<String> analysisList = new ArrayList<>();
            List<String> imageList = new ArrayList<>();
            List<String> multiChoice = new ArrayList<>();
            QuestionRequest.Answer[] answers = questionRequest.getAnswers();
            for (int i = 0; i < answers.length; i++) {
                // 该选型有图片
                String[] images = answers[i].getImages();
                if (images != null && images.length > 0) {
                    for (int j = 0; j < images.length; j++) {
                        imageList.add(images[j]);
                    }
                }
                // 设置答案内容
                allOptionList.add(answers[i].getAnswer());
                // 设置解析内容
                analysisList.add(answers[i].getAnalysis());
                // 设置下标
                if (type == 2) {
                    // 多选
                    if ("true".equalsIgnoreCase(answers[i].getIsTrue())) {
                        multiChoice.add(String.valueOf(i));
                    }
                } else {
                    // 单选和判断只有一个答案
                    if ("true".equalsIgnoreCase(answers[i].getIsTrue())) {
                        answer.setTrueOption(i + "");
                    }
                }
            }
            if (type == 2) {
                String trueOption = StringUtils.join(multiChoice, ",");
                answer.setTrueOption(trueOption);
            }
            // 设置图片信息
            String imageUrl = StringUtils.join(imageList, ",");
            answer.setImagesUrl(imageUrl);
            // 设置答案内容
            String allOption = StringUtils.join(allOptionList, ExamConstant.SEPARATOR);
            answer.setAllOption(allOption);
            // 设置答案解析
            String analysis = StringUtils.join(analysisList, ExamConstant.SEPARATOR);
            answer.setAnalysis(analysis);
            // 更新题目信息
            answerService.update(answer, new UpdateWrapper<Answer>().eq("questionId", question.getId()));
        }
    }

    @Override
    public QuestionRequest getQuestionVOById(Long questionId) {
        Question question = questionService.getById(questionId);
        QuestionRequest questionVO = new QuestionRequest();
        BeanUtils.copyProperties(question, questionVO);
        // 设置所属题库id
        String[] strBankIds = StringUtils.split(question.getBankId(), ",");
        Long[] bankIds = new Long[strBankIds.length];
        for (int i = 0; i < strBankIds.length; i++) {
            bankIds[i] = Long.parseLong(strBankIds[i]);
        }
        questionVO.setBankIds(bankIds);
        // 设置题目图片
        String contentImage = question.getContentImage();
        if (StringUtils.isNotBlank(contentImage)) {
            String[] contentImages = StringUtils.split(contentImage, ",");
            questionVO.setContentImages(contentImages);
        }
        questionVO.setCreator(question.getCreator());
        // 设置答案
        Answer answer = answerService.getOne(new QueryWrapper<Answer>().eq("questionId", questionId));
        if (answer != null) {
            Integer type = question.getType();
            if (type != 2) {
                // 为单选或者判断
                String[] allOption = StringUtils.splitByWholeSeparator(answer.getAllOption(), ExamConstant.SEPARATOR);
                String[] imagesUrl = StringUtils.splitByWholeSeparator(answer.getImagesUrl(), ",");
                String[] analysis = StringUtils.splitByWholeSeparator(answer.getAnalysis(), ExamConstant.SEPARATOR);
                QuestionRequest.Answer[] answers = new QuestionRequest.Answer[allOption.length];
                for (int i = 0; i < allOption.length; i++) {
                    QuestionRequest.Answer answer1 = new QuestionRequest.Answer();
                    answer1.setAnswer(allOption[i]);
                    answer1.setId((long) i);
                    answer1.setAnalysis(analysis[i]);
                    if (i < imagesUrl.length && StringUtils.isNotBlank(imagesUrl[i])) {
                        answer1.setImages(new String[]{imagesUrl[i]});
                    }
                    if (i == Integer.parseInt(answer.getTrueOption())) {
                        answer1.setIsTrue("true");
                    } else {
                        answer1.setIsTrue("false");
                    }
                    answers[i] = answer1;
                }
                questionVO.setAnswers(answers);
            } else {
                // 多选
                String[] allOption = StringUtils.splitByWholeSeparator(answer.getAllOption(), ExamConstant.SEPARATOR);
                String[] imagesUrl = StringUtils.splitByWholeSeparator(answer.getImagesUrl(), ",");
                String[] analysis = StringUtils.splitByWholeSeparator(answer.getAnalysis(), ExamConstant.SEPARATOR);
                String[] trueOptions = StringUtils.splitByWholeSeparator(answer.getTrueOption(), ",");
                QuestionRequest.Answer[] answers = new QuestionRequest.Answer[allOption.length];
                for (int i = 0; i < allOption.length; i++) {
                    QuestionRequest.Answer answer1 = new QuestionRequest.Answer();
                    answer1.setAnswer(allOption[i]);
                    answer1.setId((long) i);
                    answer1.setAnalysis(analysis[i]);
                    if (i < imagesUrl.length && StringUtils.isNotBlank(imagesUrl[i])) {
                        answer1.setImages(new String[]{imagesUrl[i]});
                    }
                    if (i < trueOptions.length && i == Integer.parseInt(trueOptions[i])) {
                        answer1.setIsTrue("true");
                    } else {
                        answer1.setIsTrue("false");
                    }
                    answers[i] = answer1;
                }
                questionVO.setAnswers(answers);
            }
        }
        return questionVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addQuestionToBank(String questionIds, String bankIds) {
        String[] quIds = StringUtils.split(questionIds, ",");
        String[] baIds = StringUtils.split(bankIds, ",");
        boolean flag = false;
        for (String questionId : quIds) {
            // 当前题目信息
            Question question = questionService.getById(questionId);
            String questionBankId = question.getBankId();
            // 当前题目已经在的题库id
            String[] questionBankIds = StringUtils.split(questionBankId, ",");
            Set<Long> allId = new HashSet<>();
            if (questionBankIds != null && questionBankIds.length > 0) {
                for (String bankId : questionBankIds) {
                    allId.add(Long.parseLong(bankId));
                }
            }
            // 新增的题库
            for (String bankId : baIds) {
                allId.add(Long.parseLong(bankId));
            }
            String newBankIds = StringUtils.join(allId, ",");
            // 更新当前题目的题库
            question.setBankId(newBankIds);

            List<String> bankNameList = new ArrayList<>();
            for (Long id : allId) {
                String bankName = questionBankService.getById(id).getBankName();
                bankNameList.add(bankName);
            }
            String bankName = StringUtils.join(bankNameList, ",");
            question.setBankName(bankName);
            // 更新题目信息
            flag = questionService.updateById(question);
        }
        return flag;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeQuestionFromBank(String questionIds, String bankIds) {
        boolean flag = false;
        String[] quIds = StringUtils.split(questionIds, ",");
        String[] baIds = StringUtils.split(bankIds, ",");
        for (String questionId : quIds) {
            // 当前题目信息
            Question question = questionService.getById(questionId);
            String questionBankId = question.getBankId();
            // 当前问题拥有的题库id
            String[] questionBankIds = StringUtils.split(questionBankId, ",");
            Set<Long> handleId = new HashSet<>();
            if (questionBankIds != null && questionBankIds.length > 0) {
                for (String bankId : questionBankIds) {
                    handleId.add(Long.parseLong(bankId));
                }
            }
            for (String bankId : baIds) {
                handleId.remove(Long.parseLong(bankId));
            }
            String newBankIds = StringUtils.join(handleId, ",");
            List<String> bankNameList = new ArrayList<>();
            if (StringUtils.isNotBlank(newBankIds)) {
                // 更新题库id集合
                question.setBankId(newBankIds);
                // 更新删除后还剩余的题库名称
                for (Long id : handleId) {
                    String bankName = questionBankService.getById(id).getBankName();
                    bankNameList.add(bankName);
                }
                String bankName = StringUtils.join(bankNameList, ",");
                question.setBankName(bankName);
            } else {
                // 没有所属题库了
                question.setBankId(null);
                question.setBankName(null);
            }
            // 更新问题
            flag = questionService.updateById(question);
        }
        return flag;
    }
}




