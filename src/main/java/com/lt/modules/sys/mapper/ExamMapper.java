package com.lt.modules.sys.mapper;

import com.lt.modules.sys.model.entity.Exam;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author teng
 * @description 针对表【exam(考试表)】的数据库操作Mapper
 * @createDate 2022-11-22 09:24:12
 * @Entity com.lt.modules.sys.model.entity.Exam
 */
@Repository
public interface ExamMapper extends BaseMapper<Exam> {

    List<Exam> getMyExamInfo(Long userId);
}




