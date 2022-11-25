package com.lt.modules.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lt.common.BaseResponse;
import com.lt.common.ErrorCode;
import com.lt.common.annotation.SysLog;
import com.lt.common.utils.DateUtils;
import com.lt.common.utils.ResultUtils;
import com.lt.modules.sys.model.dto.exam.AddExamByQuestionListRequest;
import com.lt.modules.sys.model.entity.Exam;
import com.lt.modules.sys.model.entity.ExamQuestion;
import com.lt.modules.sys.model.entity.Organ;
import com.lt.modules.sys.model.entity.User;
import com.lt.modules.sys.service.ExamQuestionService;
import com.lt.modules.sys.service.ExamService;
import com.lt.modules.sys.service.OrganService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @description: 考试管理
 * @author: ~Teng~
 * @date: 2022/11/24 14:24
 */
@RestController
@RequestMapping("/sys/exam")
public class ExamController extends AbstractController {

    @Autowired
    private ExamService examService;

    @Autowired
    private OrganService organService;

    @Autowired
    private ExamQuestionService examQuestionService;

    /**
     * 获取考试信息
     */
    @GetMapping("/getExamInfo")
    public BaseResponse getExamInfo(Integer pageNo, Integer pageSize,
                                    @RequestParam(required = false) String examName,
                                    @RequestParam(required = false) String startTime,
                                    @RequestParam(required = false) String endTime,
                                    @RequestParam(required = false) Long organId) {
        if (pageNo == null || pageNo <= 0 || pageSize == null || pageSize <= 0) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "参数错误");
        }
        Page<Exam> page = examService.getExamInfo(pageNo, pageSize, examName, startTime, endTime, organId);
        return ResultUtils.success(page);
    }

    /**
     * 根据考试id获取考试信息
     */
    @GetMapping("/getExamInfoById/{examId}")
    public BaseResponse getExamInfoById(@PathVariable("examId") Long examId) {
        if (examId == null || examId <= 0) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "参数错误");
        }
        Exam exam = examService.getById(examId);
        if (exam == null) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "查询失败");
        }
        AddExamByQuestionListRequest ExamVO = new AddExamByQuestionListRequest();
        BeanUtils.copyProperties(exam, ExamVO);
        ExamQuestion examQuestion = examQuestionService.getOne(new QueryWrapper<ExamQuestion>().eq("examId", exam.getId()));
        BeanUtils.copyProperties(examQuestion, ExamVO);
        return ResultUtils.success(ExamVO);
    }

    @SysLog("根据题目列表添加考试")
    @PostMapping("/addExamByQuestionList")
    public BaseResponse addExamByQuestionList(@RequestBody AddExamByQuestionListRequest addExamByQuestionListRequest) {
        addExamByQuestionListRequest.setCreator(getUser().getUsername());
        String examName = addExamByQuestionListRequest.getExamName();
        String examDesc = addExamByQuestionListRequest.getExamDesc();
        if (StringUtils.isAnyBlank(examName, examDesc)) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "考试名称或描述不允许为空");
        }
        Date startTime = addExamByQuestionListRequest.getStartTime();
        Date endTime = addExamByQuestionListRequest.getEndTime();
        Integer totalTime = addExamByQuestionListRequest.getTotalTime();
        if (startTime == null || endTime == null || totalTime == null || totalTime <= 0) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "考试时间不允许为空");
        }
        Integer passScore = addExamByQuestionListRequest.getPassScore();
        if (passScore == null || passScore < 50) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "及格线设置不合理");
        }
        String questionIds = addExamByQuestionListRequest.getQuestionIds();
        String scores = addExamByQuestionListRequest.getScores();
        if (StringUtils.isAnyBlank(questionIds, scores)) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "所选题目和分数不能为空");
        }
        User user = getUser();
        Long organId = user.getOrganId();
        if (organId == null) {
            // 为管理员 管理员添加的试卷不属于任何一个机构
            addExamByQuestionListRequest.setOrganId(null);
            addExamByQuestionListRequest.setOrganName(null);
        } else {
            Organ organ = organService.getById(organId);
            addExamByQuestionListRequest.setOrganId(organId);
            addExamByQuestionListRequest.setOrganName(organ.getName());
        }
        examService.addExamByQuestionList(addExamByQuestionListRequest);
        return ResultUtils.success("添加成功");
    }

    @SysLog("更新考试信息")
    @PostMapping("/updateExamInfo")
    public BaseResponse updateExamInfo(@RequestBody AddExamByQuestionListRequest addExamByQuestionListRequest) {
        // 数据校验
        String examName = addExamByQuestionListRequest.getExamName();
        String examDesc = addExamByQuestionListRequest.getExamDesc();
        String questionIds = addExamByQuestionListRequest.getQuestionIds();
        String scores = addExamByQuestionListRequest.getScores();
        if (StringUtils.isAnyBlank(examName, examDesc, questionIds, scores)) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "请添加完整考试信息");
        }
        Integer totalTime = addExamByQuestionListRequest.getTotalTime();
        Date startTime = addExamByQuestionListRequest.getStartTime();
        Date endTime = addExamByQuestionListRequest.getEndTime();
        if (totalTime == null || startTime == null || endTime == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "请添加完整考试信息");
        }
        Integer passScore = addExamByQuestionListRequest.getPassScore();
        if (totalTime <= 0 || passScore == null || passScore <= 50) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "添加数据不合理");
        }
        Date realEndTime = DateUtils.addDateSeconds(startTime, totalTime);
        if (endTime.getTime() != realEndTime.getTime()) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "结束时间添加不正确");
        }
        addExamByQuestionListRequest.setUpdater(getUser().getUsername());
        examService.updateExamInfo(addExamByQuestionListRequest);
        return ResultUtils.success("更新成功");
    }
}
