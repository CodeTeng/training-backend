package com.lt.modules.sys.service;

import com.lt.modules.sys.model.dto.examRecord.AddExamRecordRequest;
import com.lt.modules.sys.model.entity.ExamRecord;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author teng
 * @description 针对表【exam_record(考试记录表)】的数据库操作Service
 * @createDate 2022-11-22 10:03:04
 */
public interface ExamRecordService extends IService<ExamRecord> {

    void addExamRecord(AddExamRecordRequest addExamRecordRequest, ExamRecord examRecord);
}
