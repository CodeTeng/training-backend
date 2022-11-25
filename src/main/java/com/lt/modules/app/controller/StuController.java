package com.lt.modules.app.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lt.common.BaseResponse;
import com.lt.common.ErrorCode;
import com.lt.common.exception.BusinessException;
import com.lt.common.utils.ResultUtils;
import com.lt.modules.app.model.dto.UserRegisterRequest;
import com.lt.modules.sys.model.entity.ExamRecord;
import com.lt.modules.sys.service.ExamRecordService;
import com.lt.modules.app.service.StuService;
import com.lt.modules.sys.controller.AbstractController;
import com.lt.modules.sys.model.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
     * 获取学员个人成绩（分页 可根据考试名查询）
     */
    @GetMapping("/getMyGrade")
    public BaseResponse getMyGrade(Integer pageNo, Integer pageSize,
                                   @RequestParam(required = false) Long examId) {
        User user = getUser();
        IPage<ExamRecord> examRecordIPage = new Page<>(pageNo, pageSize);
        QueryWrapper<ExamRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", user.getCreator());
        if (examId != null) {
            queryWrapper.eq("examId", examId);
        }
        IPage<ExamRecord> page = examRecordService.page(examRecordIPage, queryWrapper);
        return ResultUtils.success(page);
    }


}
