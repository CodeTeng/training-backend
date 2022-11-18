package com.lt.modules.sys.controller;

import com.lt.common.annotation.SysLog;
import com.lt.common.utils.PageUtils;
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
     * 分页用户列表
     */
    @GetMapping("/list")
    @RequiresPermissions("sys:user:list")
    public BaseResponse list(@RequestParam Map<String, Object> params) {
        User user = userService.getById(getUserId());
        if (user == null) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "操作失败");
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
        String password = userPasswordRequest.getPassword();
        String newPassword = userPasswordRequest.getNewPassword();
        if (password.equals(newPassword)) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "新密码不能和原密码一样");
        }
        // sha256加密
        String encryptedPassword = new Sha256Hash(userPasswordRequest.getPassword()).toHex();
        // sha256加密
        String encryptedNewPassword = new Sha256Hash(userPasswordRequest.getNewPassword()).toHex();
        // 更新密码
        boolean flag = userService.updatePassword(getUserId(), encryptedPassword, encryptedNewPassword);
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

    /**
     * 修改用户
     */
    @SysLog("修改用户")
    @PostMapping("/update")
    @RequiresPermissions("sys:user:update")
    public BaseResponse update(@RequestBody User user) {
        User curUser = userService.getById(getUserId());
        user.setUpdater(curUser.getUsername());
        userService.update(user);
        return ResultUtils.success(true);
    }

    /**
     * 删除用户-不支持批量删除 批量删除反序列化错误
     */
    @SysLog("删除用户")
    @PostMapping("/delete/{id}")
    @RequiresPermissions("sys:user:delete")
    public BaseResponse delete(@PathVariable("id") Long id) {
        if (id == null || id <= 0) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "请求参数错误");
        }
        if (id == 1L) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "超级管理员不能删除");
        }
        if (id.equals(getUserId())) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "当前用户不能删除");
        }
        User user = userService.getById(id);
        user.setUpdater(getUser().getUsername());
        userService.updateById(user);
        boolean flag = userService.removeById(id);
        // TODO 反序列化错误
//        userService.deleteBatch(userIds);
        return ResultUtils.success(flag);
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
