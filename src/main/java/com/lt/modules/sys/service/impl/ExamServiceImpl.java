package com.lt.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.modules.sys.model.dto.exam.AddExamByQuestionListRequest;
import com.lt.modules.sys.model.entity.Exam;
import com.lt.modules.sys.model.entity.ExamQuestion;
import com.lt.modules.sys.service.ExamQuestionService;
import com.lt.modules.sys.service.ExamService;
import com.lt.modules.sys.mapper.ExamMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author teng
 * @description 针对表【exam(考试表)】的数据库操作Service实现
 * @createDate 2022-11-22 09:24:12
 */
@Service
public class ExamServiceImpl extends ServiceImpl<ExamMapper, Exam>
        implements ExamService {

    @Autowired
    private ExamQuestionService examQuestionService;

    @Override
    public Page<Exam> getExamInfo(Integer pageNo, Integer pageSize, String examName, String startTime, String endTime, Long organId) {
        QueryWrapper<Exam> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(examName), "examName", examName);
        queryWrapper.ge(StringUtils.isNotBlank(startTime), "startTime", startTime);
        queryWrapper.le(StringUtils.isNotBlank(endTime), "endTime", endTime);
        queryWrapper.eq(organId != null, "organId", organId);
        Page<Exam> page = this.page(
                new Page<>(pageNo, pageSize),
                queryWrapper
        );
        return page;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addExamByQuestionList(AddExamByQuestionListRequest addExamByQuestionListRequest) {
        // 先添加试卷
        Exam exam = new Exam();
        BeanUtils.copyProperties(addExamByQuestionListRequest, exam);
        // 添加数据库中
        this.save(exam);
        // 设置考试的题目和分值信息
        ExamQuestion examQuestion = new ExamQuestion();
        BeanUtils.copyProperties(addExamByQuestionListRequest, examQuestion);
        examQuestion.setExamId(exam.getId());
        examQuestionService.save(examQuestion);
    }

    @Override
    public void updateExamInfo(AddExamByQuestionListRequest addExamByQuestionListRequest) {
        Exam exam = new Exam();
        BeanUtils.copyProperties(addExamByQuestionListRequest, exam);
        this.updateById(exam);
        // 更新考试题目信息
        ExamQuestion examQuestion = new ExamQuestion();
        examQuestion.setExamId(exam.getId());
        examQuestion.setQuestionIds(addExamByQuestionListRequest.getQuestionIds());
        examQuestion.setScores(addExamByQuestionListRequest.getScores());
        examQuestionService.update(examQuestion, new UpdateWrapper<ExamQuestion>().eq("examId", exam.getId()));
    }
}




