package com.lt.modules.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.modules.sys.model.entity.ExamQuestion;
import com.lt.modules.sys.service.ExamQuestionService;
import com.lt.modules.sys.mapper.ExamQuestionMapper;
import org.springframework.stereotype.Service;

/**
* @author teng
* @description 针对表【exam_question(考试题目关联表)】的数据库操作Service实现
* @createDate 2022-11-22 09:24:12
*/
@Service
public class ExamQuestionServiceImpl extends ServiceImpl<ExamQuestionMapper, ExamQuestion>
    implements ExamQuestionService{

}




