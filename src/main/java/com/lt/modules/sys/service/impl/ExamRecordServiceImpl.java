package com.lt.modules.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.modules.sys.model.entity.ExamRecord;
import com.lt.modules.sys.service.ExamRecordService;
import com.lt.modules.sys.mapper.ExamRecordMapper;
import org.springframework.stereotype.Service;

/**
* @author teng
* @description 针对表【exam_record(考试记录表)】的数据库操作Service实现
* @createDate 2022-11-22 10:03:04
*/
@Service
public class ExamRecordServiceImpl extends ServiceImpl<ExamRecordMapper, ExamRecord>
    implements ExamRecordService{

}




