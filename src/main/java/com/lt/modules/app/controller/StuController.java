package com.lt.modules.app.controller;

import com.lt.common.BaseResponse;
import com.lt.common.ErrorCode;
import com.lt.common.exception.BusinessException;
import com.lt.common.utils.ResultUtils;
import com.lt.modules.app.entity.dto.UserRegisterRequest;
import com.lt.modules.app.service.StuService;
import com.lt.modules.sys.controller.AbstractController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
