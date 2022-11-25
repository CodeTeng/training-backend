package com.lt.modules.sys.controller;

import ch.qos.logback.core.recovery.ResilientFileOutputStream;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lt.common.annotation.SysLog;
import com.lt.modules.sys.model.dto.user.*;
import com.lt.modules.sys.model.entity.Organ;
import com.lt.modules.sys.model.entity.User;
import com.lt.modules.sys.model.vo.UserInfoVO;
import com.lt.modules.sys.service.OrganService;
import com.lt.modules.sys.service.UserRoleService;
import com.lt.modules.sys.service.UserService;
import com.lt.common.BaseResponse;
import com.lt.common.ErrorCode;
import com.lt.common.utils.ResultUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.PaddingScheme;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @Autowired
    private OrganService organService;

    /**
     * 分页用户列表
     */
    @GetMapping("/list")
    @RequiresPermissions("sys:user:list")
    public BaseResponse list(Integer pageNo, Integer pageSize,
                             @RequestParam(required = false) String username,
                             @RequestParam(required = false) String organName) {
        if (pageNo == null || pageNo <= 0 || pageSize == null || pageSize <= 0) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "参数错误");
        }
        Page<UserInfoVO> page = userService.queryPage(pageNo, pageSize, username, organName);
        return ResultUtils.success(page);
    }

    /**
     * 获取登录的用户信息
     */
    @GetMapping("/info")
    public BaseResponse info() {
        User user = getUser();
        List<String> userRoleName = userRoleService.getUserRoleName(user.getId());
        String roleName = StringUtils.join(userRoleName, ",");
        UserInfoVO userInfoVO = new UserInfoVO();
        BeanUtils.copyProperties(user, userInfoVO);
        userInfoVO.setRoleName(roleName);
        Organ organ = organService.getById(user.getOrganId());
        if (organ == null) {
            userInfoVO.setOrganName(null);
        } else {
            userInfoVO.setOrganName(organ.getName());
        }
        return ResultUtils.success(userInfoVO);
    }

    /**
     * 修改用户密码
     */
    @SysLog("修改密码")
    @PostMapping("/password")
    @RequiresPermissions("sys:user:update")
    public BaseResponse password(@RequestBody @Validated UserPasswordRequest userPasswordRequest) {
        String password = userPasswordRequest.getPassword();
        String newPassword = userPasswordRequest.getNewPassword();
        if (password.length() < 6 || newPassword.length() < 6) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "密码最短为6位");
        }
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
     * 查询单个用户信息
     */
    @GetMapping("/info/{userId}")
    @RequiresPermissions("sys:user:info")
    @SysLog("查询用户信息")
    public BaseResponse getUserInfo(@PathVariable("userId") Long userId) {
        if (userId == null || userId <= 0) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "请求参数错误");
        }
        User user = userService.getById(userId);
        List<String> userRoleName = userRoleService.getUserRoleName(userId);
        String roleName = StringUtils.join(userRoleName, ",");
        UserInfoVO userInfoVO = new UserInfoVO();
        BeanUtils.copyProperties(user, userInfoVO);
        userInfoVO.setRoleName(roleName);
        Organ organ = organService.getById(user.getOrganId());
        if (organ == null) {
            userInfoVO.setOrganName(null);
        } else {
            userInfoVO.setOrganName(organ.getName());
        }
        return ResultUtils.success(userInfoVO);
    }

    @SysLog("重置密码")
    @RequiresPermissions("sys:user:update")
    @PostMapping("/reset/{userId}")
    public BaseResponse resetPassword(@PathVariable("userId") Long userId, String password) {
        if (userId == null || userId <= 0) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "请求参数错误");
        }
        User user = userService.getById(userId);
        if (password == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "密码不能为空");
        }
        if (password.length() < 6) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "密码最短为6位");
        }
        String encryptedPassword = new Sha256Hash(password).toHex();
        user.setPassword(encryptedPassword);
        user.setUpdater(getUser().getUsername());
        userService.updateById(user);
        return ResultUtils.success("重置成功");
    }

    /**
     * 保存用户
     */
    @SysLog("保存用户")
    @PostMapping("/save")
    @RequiresPermissions("sys:user:save")
    public BaseResponse save(@RequestBody @Validated UserAddRequest userAddRequest) {
        String password = userAddRequest.getPassword();
        if (password.length() < 6) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "密码最短为6位");
        }
        User curUser = userService.getById(getUserId());
        userAddRequest.setCreator(curUser.getUsername());
        userService.saveUser(userAddRequest);
        return ResultUtils.success(true);
    }

    /**
     * 修改用户
     */
    @SysLog("修改用户")
    @PostMapping("/update")
    @RequiresPermissions("sys:user:update")
    public BaseResponse update(@RequestBody @Validated UserUpdateRequest userUpdateRequest) {
        userUpdateRequest.setUpdater(getUser().getUsername());
        userService.update(userUpdateRequest);
        return ResultUtils.success(true);
    }

    /**
     * 删除用户-不支持批量删除 批量删除反序列化错误
     */
    @SysLog("删除用户")
    @PostMapping("/delete/{id}")
    @RequiresPermissions("sys:user:delete")
    @Transactional(rollbackFor = Exception.class)
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
        if (!flag) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "删除失败");
        }
        // TODO 反序列化错误
//        userService.deleteBatch(userIds);
        return ResultUtils.success(flag);
    }
}
