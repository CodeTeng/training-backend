package com.lt.modules.sys.controller;

import com.lt.common.annotation.SysLog;
import com.lt.common.utils.PageUtils;
import com.lt.constant.UserConstant;
import com.lt.modules.sys.model.dto.user.*;
import com.lt.modules.sys.model.entity.User;
import com.lt.modules.sys.model.vo.UserVO;
import com.lt.modules.sys.service.UserRoleService;
import com.lt.modules.sys.service.UserService;
import com.lt.common.BaseResponse;
import com.lt.common.ErrorCode;
import com.lt.common.utils.ResultUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 系统用户接口
 *
 * @author teng
 */
@RestController
@RequestMapping("/sys/user")
public class UserController extends AbstractController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRoleService userRoleService;

    /**
     * 所有用户列表---包含审核和停用的用户
     */
    @GetMapping("/list")
    @RequiresPermissions("sys:user:list")
    @SysLog("查看用户列表")
    public BaseResponse list(@RequestParam Map<String, Object> params) {
        User user = userService.getById(getUserId());
        if (user == null) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "查看失败");
        }
        PageUtils page = userService.queryPage(params);
        return ResultUtils.success(page);
    }

    /**
     * 获取登录的用户信息
     */
    @GetMapping("/info")
    public BaseResponse info() {
        User user = getUser();
        // 数据脱敏
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return ResultUtils.success(userVO);
    }

    /**
     * 修改登录用户密码
     */
    @SysLog("修改密码")
    @PostMapping("/password")
    @RequiresPermissions("sys:user:update")
    public BaseResponse password(@RequestBody UserPasswordRequest userPasswordRequest) {
        // sha256加密
        String password = new Sha256Hash(userPasswordRequest.getPassword()).toHex();
        // sha256加密
        String newPassword = new Sha256Hash(userPasswordRequest.getNewPassword()).toHex();
        // 更新密码
        boolean flag = userService.updatePassword(getUserId(), password, newPassword);
        if (!flag) {
            return ResultUtils.error(ErrorCode.UPDATE_ERROR, "密码错误");
        }
        return ResultUtils.success(flag);
    }

    /**
     * 查询用户信息
     */
    @GetMapping("/info/{userId}")
    @RequiresPermissions("sys:user:info")
    @SysLog("查询用户信息")
    public BaseResponse getUserInfo(@PathVariable("userId") Long userId) {
        if (userId == null || userId <= 0) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "请求参数错误");
        }
        User user = userService.getById(userId);
        // 获取用户所属的角色列表
        List<Long> roleIdList = userRoleService.queryRoleIdList(userId);
        user.setRoleIdList(roleIdList);
        return ResultUtils.success(user);
    }

    /**
     * 保存用户
     */
    @SysLog("保存用户")
    @PostMapping("/save")
    @RequiresPermissions("sys:user:save")
    public BaseResponse save(@RequestBody User user) {
        User curUser = userService.getById(getUserId());
        user.setCreator(curUser.getUsername());
        userService.saveUser(user);
        return ResultUtils.success(true);
    }

//    /**
//     * 用户注册---需要管理员审核通过后才算真正注册成功
//     */
//    @PostMapping("/register")
//    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
//        if (userRegisterRequest == null) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR);
//        }
//        String username = userRegisterRequest.getUsername();
//        String password = userRegisterRequest.getPassword();
//        String nickname = userRegisterRequest.getNickname();
//        String checkPassword = userRegisterRequest.getCheckPassword();
//        if (StringUtils.isAnyBlank(username, password, nickname, checkPassword)) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR);
//        }
//        long result = userService.userRegister(username, password, nickname, checkPassword);
//        return ResultUtils.success(result);
//    }
}
