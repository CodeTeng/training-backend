package com.lt.modules.app.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lt.common.BaseResponse;
import com.lt.common.ErrorCode;
import com.lt.common.exception.BusinessException;
import com.lt.common.utils.ResultUtils;
import com.lt.modules.app.model.dto.UserRegisterRequest;
import com.lt.modules.sys.model.entity.Exam;
import com.lt.modules.sys.model.entity.ExamRecord;
import com.lt.modules.sys.model.vo.exam.ExamStateVO;
import com.lt.modules.sys.service.ExamRecordService;
import com.lt.modules.app.service.StuService;
import com.lt.modules.sys.controller.AbstractController;
import com.lt.modules.sys.model.entity.User;
import com.lt.modules.sys.service.ExamService;
import com.lt.modules.sys.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 学员前台管理
 * @author: ~Teng~
 * @date: 2022/11/20 20:31
 */
@RestController
@RequestMapping("/app/user")
public class StuController extends AbstractController {

    @Autowired
    private StuService stuService;

    @Autowired
    private ExamRecordService examRecordService;

    @Autowired
    private ExamService examService;

    /**
     * 用户注册---需要管理员审核通过后才算真正注册成功
     */
    @PostMapping("/register")
    public BaseResponse userRegister(@RequestBody @Validated UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String username = userRegisterRequest.getUsername();
        if (username.length() < 4) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "用户名最短为4位");
        }
        String password = userRegisterRequest.getPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (password.length() < 6 || checkPassword.length() < 6) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "密码最短为6位");
        }
        long result = stuService.userRegister(userRegisterRequest);
        return ResultUtils.success(result);
    }

    /**
     * 获取学员个人成绩
     */
    @GetMapping("/getMyGrade")
    @RequiresPermissions("app:record:all")
    public BaseResponse getMyGrade(Long examId) {
        if (examId == null || examId <= 0) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "参数错误");
        }
        QueryWrapper<ExamRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", getUserId());
        queryWrapper.eq("examId", examId);
        List<ExamRecord> list = examRecordService.list(queryWrapper);
        return ResultUtils.success(list);
    }

    /**
     * 查询本人的所有考试信息
     */
    @GetMapping("/getMyExamInfo")
    @RequiresPermissions("app:exam:all")
    public BaseResponse getMyExamInfo() {
        Long userId = getUserId();
        List<Exam> exams = examService.getMyExamInfo(userId);
        return ResultUtils.success(exams);
    }
}
