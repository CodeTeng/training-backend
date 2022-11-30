package com.lt.modules.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lt.common.BaseResponse;
import com.lt.common.ErrorCode;
import com.lt.common.annotation.SysLog;
import com.lt.common.utils.ResultUtils;
import com.lt.modules.sys.model.dto.examRecord.AddExamRecordRequest;
import com.lt.modules.sys.model.entity.Exam;
import com.lt.modules.sys.model.entity.ExamRecord;
import com.lt.modules.sys.model.entity.Organ;
import com.lt.modules.sys.model.entity.User;
import com.lt.modules.sys.service.ExamRecordService;
import com.lt.modules.sys.service.ExamService;
import com.lt.modules.sys.service.OrganService;
import com.lt.modules.sys.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description: 考试记录
 * @author: ~Teng~
 * @date: 2022/11/22 10:10
 */
@RestController
@RequestMapping("/sys/examRecord")
public class ExamRecordController extends AbstractController {

    @Autowired
    private ExamRecordService examRecordService;

    @Autowired
    private ExamService examService;

    @Autowired
    private OrganService organService;

    @Autowired
    private UserService userService;

    /**
     * 获取考试记录信息
     */
    @GetMapping("/getExamRecord")
    @RequiresPermissions("sys:record:list")
    public BaseResponse getExamRecord(Integer pageNo, Integer pageSize,
                                      @RequestParam(required = false) Long examId,
                                      @RequestParam(required = false) String username,
                                      @RequestParam(required = false) Long organId) {
        if (pageNo == null || pageSize == null || pageNo <= 0 || pageSize <= 0) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "参数错误");
        }
        QueryWrapper<ExamRecord> queryWrapper = new QueryWrapper<>();
        List<User> userList = userService.list(new QueryWrapper<User>().like(StringUtils.isNotBlank(username), "username", username));
        if (userList != null && userList.size() > 0) {
            List<Long> userIdList = userList.stream().map(User::getId).toList();
            queryWrapper.in("userId", userIdList);
        } else {
            queryWrapper.in("userId", 0);
        }
        queryWrapper.eq(examId != null && examId > 0, "examId", examId);
        queryWrapper.eq(organId != null && organId > 0, "organId", organId);
        Page<ExamRecord> page = examRecordService.page(
                new Page<>(pageNo, pageSize),
                queryWrapper
        );
        return ResultUtils.success(page);
    }

    /**
     * 根据考试的记录id查询用户考试的信息
     */
    @GetMapping("/getExamRecordById/{recordId}")
    @RequiresPermissions("sys:record:list")
    public BaseResponse getExamRecordById(@PathVariable("recordId") Long recordId) {
        if (recordId == null || recordId <= 0) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "请求参数错误");
        }
        ExamRecord examRecord = examRecordService.getById(recordId);
        if (examRecord == null) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "查询失败");
        }
        return ResultUtils.success(examRecord);
    }

    /**
     * 设置考试记录的客观题得分,设置总分为逻辑得分+客观题
     */
    @PostMapping("/setReview")
    @SysLog("进行批阅试卷")
    @RequiresPermissions("sys:record:update")
    public BaseResponse setReview(Long examRecordId, Integer totalScore) {
        if (examRecordId == null || examRecordId <= 0 || totalScore == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "参数错误");
        }
        if (totalScore < 0) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "总分最低为0分");
        }
        ExamRecord examRecord = examRecordService.getById(examRecordId);
        examRecord.setTotalScore(totalScore);
        examRecord.setUpdater(getUser().getUsername());
        boolean flag = examRecordService.updateById(examRecord);
        if (!flag) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "批阅失败");
        }
        return ResultUtils.success("批阅成功");
    }

    @SysLog("保存考试记录")
    @PostMapping("/addExamRecord")
    @RequiresPermissions("sys:record:save")
    public BaseResponse addExamRecord(@RequestBody AddExamRecordRequest addExamRecordRequest) {
        Long examId = addExamRecordRequest.getExamId();
        if (examId == null || examId <= 0) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "参数错误");
        }
        String questionIds = addExamRecordRequest.getQuestionIds();
        if (StringUtils.isBlank(questionIds)) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "考试不能没有题目");
        }
        ExamRecord examRecord = new ExamRecord();
        examRecord.setUserId(getUserId());
        examRecord.setCreator(getUser().getUsername());
        examRecord.setExamId(examId);
        examRecord.setQuestionIds(questionIds);
        String userAnswers = addExamRecordRequest.getUserAnswers();
        Exam exam = examService.getById(examId);
        Date startTime = exam.getStartTime();
        Date endTime = exam.getEndTime();
        if (startTime.getTime() > System.currentTimeMillis()) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "考试未开始");
        }
        if (endTime.getTime() < System.currentTimeMillis()) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "考试已经结束");
        }
        if (StringUtils.isBlank(userAnswers)) {
            // 直接交白卷
            examRecord.setExamTime(new Date());
            examRecord.setLogicScore(0);
            examRecord.setOrganId(getUser().getOrganId());
            Organ organ = organService.getById(getUser().getOrganId());
            if (organ == null) {
                examRecord.setOrganName(null);
            } else {
                examRecord.setOrganName(organ.getName());
            }
            examRecord.setUserAnswers(null);
            examRecord.setErrorQuestionIds(addExamRecordRequest.getQuestionIds());
            examRecord.setCreditImgUrl(null);
            examRecordService.save(examRecord);
            return ResultUtils.success("添加成功");
        }
        examRecord.setUserAnswers(addExamRecordRequest.getUserAnswers());
        examRecordService.addExamRecord(addExamRecordRequest, examRecord);
        return ResultUtils.success("添加成功");
    }
}
