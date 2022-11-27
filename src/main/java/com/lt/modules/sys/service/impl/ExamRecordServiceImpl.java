package com.lt.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.modules.sys.model.dto.examRecord.AddExamRecordRequest;
import com.lt.modules.sys.model.entity.Answer;
import com.lt.modules.sys.model.entity.Exam;
import com.lt.modules.sys.model.entity.ExamQuestion;
import com.lt.modules.sys.model.entity.ExamRecord;
import com.lt.modules.sys.service.AnswerService;
import com.lt.modules.sys.service.ExamQuestionService;
import com.lt.modules.sys.service.ExamRecordService;
import com.lt.modules.sys.mapper.ExamRecordMapper;
import com.lt.modules.sys.service.ExamService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author teng
 * @description 针对表【exam_record(考试记录表)】的数据库操作Service实现
 * @createDate 2022-11-22 10:03:04
 */
@Service
public class ExamRecordServiceImpl extends ServiceImpl<ExamRecordMapper, ExamRecord>
        implements ExamRecordService {

    @Autowired
    private ExamService examService;

    @Autowired
    private ExamQuestionService examQuestionService;

    @Autowired
    private AnswerService answerService;

    @Override
    public void addExamRecord(AddExamRecordRequest addExamRecordRequest, ExamRecord examRecord) {
        Exam exam = examService.getOne(new QueryWrapper<Exam>().eq("id", examRecord.getExamId()));
        examRecord.setOrganId(exam.getOrganId());
        examRecord.setOrganName(exam.getOrganName());
        // 设置题目和每道题目的分数
        ExamQuestion examQuestion = examQuestionService.getOne(new QueryWrapper<ExamQuestion>().eq("examId", examRecord.getExamId()));
        // 题目id集合
        String[] questionIds = addExamRecordRequest.getQuestionIds().split(",");
        // 每道题目的分数
        String[] scores = examQuestion.getScores().split(",");
        // key 为题目 id value 为 该题的分数
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < questionIds.length; i++) {
            map.put(questionIds[i], scores[i]);
        }
        // 查询每道题目的正确答案
        List<Answer> answers = answerService.list(new QueryWrapper<Answer>().in("questionId", questionIds));
        // 用户的答案
        String[] userAnswers = addExamRecordRequest.getUserAnswers().split("-");
        // 逻辑得分
        int logicScore = 0;
        // 错题id集合
        List<String> errorQuestionIdList = new ArrayList<>();
        for (int i = 0; i < questionIds.length; i++) {
            // 获取该题目的答案索引
            int index = getIndex(answers, Long.parseLong(questionIds[i]));
            if (index != -1) {
                // 判断是否正确
                String trueOption = answers.get(index).getTrueOption();
                String[] trueOptions = StringUtils.split(trueOption, ",");
                List<String> trueOptionList = Arrays.stream(trueOptions).toList();
                if (trueOptions.length > 1) {
                    // 多选 先记录该题分数
                    int score = Integer.parseInt(map.get(questionIds[i]));
                    String[] userAnswer = userAnswers[i].split(",");
                    int count = 0;
                    for (String s : userAnswer) {
                        if (trueOptionList.contains(s)) {
                            count++;
                        } else {
                            // 有一项选错 直接0分 并且记录错题
                            count = 0;
                            errorQuestionIdList.add(questionIds[i]);
                            break;
                        }
                    }
                    // 判断是否是全部判断正确
                    int userScore = (count / 4) * score;
                    if (userScore != score) {
                        // 是错题 添加
                        if (!errorQuestionIdList.contains(questionIds[i])) {
                            // 未重复 需要添加
                            errorQuestionIdList.add(questionIds[i]);
                        }
                    }
                    logicScore += userScore;
                } else {
                    // 单选和判断
                    if (trueOptions[0].equals(userAnswers[i])) {
                        // 正确 加分
                        logicScore += Integer.parseInt(map.get(questionIds[i]));
                    } else {
                        // 记录错题
                        errorQuestionIdList.add(questionIds[i]);
                    }
                }
            }
        }
        // 设置逻辑得分
        examRecord.setLogicScore(logicScore);
        // 如果存在错题
        if (errorQuestionIdList.size() > 0) {
            String errorQuestionIds = StringUtils.join(errorQuestionIdList, ",");
            examRecord.setErrorQuestionIds(errorQuestionIds);
        }
        // 记录考生作答时间
        examRecord.setExamTime(new Date());
        // 保存
        this.save(examRecord);
    }

    /**
     * 根据题目id获取答案列表中的答案索引
     */
    private int getIndex(List<Answer> answers, Long questionId) {
        for (int i = 0; i < answers.size(); i++) {
            if (Objects.equals(answers.get(i).getQuestionId(), questionId)) {
                return i;
            }
        }
        return -1;
    }
}




