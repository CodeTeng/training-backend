package com.lt.modules.sys.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lt.modules.sys.model.dto.exam.AddExamByQuestionListRequest;
import com.lt.modules.sys.model.entity.Exam;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;
import java.util.List;

/**
 * @author teng
 * @description 针对表【exam(考试表)】的数据库操作Service
 * @createDate 2022-11-22 09:24:12
 */
public interface ExamService extends IService<Exam> {

    Page<Exam> getExamInfo(Integer pageNo, Integer pageSize, String examName, String startTime, String endTime, Long organId);

    void addExamByQuestionList(AddExamByQuestionListRequest addExamByQuestionListRequest);

    void updateExamInfo(AddExamByQuestionListRequest addExamByQuestionListRequest);

    List<Exam> getMyExamInfo(Long userId);
}
