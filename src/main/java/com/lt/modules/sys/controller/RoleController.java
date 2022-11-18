package com.lt.modules.sys.controller;

import com.lt.common.BaseResponse;
import com.lt.common.ErrorCode;
import com.lt.common.annotation.SysLog;
import com.lt.common.utils.PageUtils;
import com.lt.common.utils.ResultUtils;
import com.lt.modules.sys.model.entity.Role;
import com.lt.modules.sys.model.entity.User;
import com.lt.modules.sys.service.RoleMenuService;
import com.lt.modules.sys.service.RoleService;
import com.lt.modules.sys.service.UserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @description: 角色管理
 * @author: ~Teng~
 * @date: 2022/11/18 16:46
 */
@RestController
@RequestMapping("/sys/role")
public class RoleController extends AbstractController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleMenuService roleMenuService;

    /**
     * 分页角色列表
     */
    @GetMapping("/list")
    @RequiresPermissions("sys:role:list")
    public BaseResponse list(@RequestParam Map<String, Object> params) {
        PageUtils page = roleService.queryPage(params);
        return ResultUtils.success(page);
    }

    /**
     * 所有角色列表
     */
    @GetMapping("/select")
    @RequiresPermissions("sys:role:select")
    @SysLog("查看角色列表")
    public BaseResponse select() {
        List<Role> list = roleService.list();
        return ResultUtils.success(list);
    }

    /**
     * 角色信息
     */
    @GetMapping("/info/{roleId}")
    @RequiresPermissions("sys:role:info")
    public BaseResponse info(@PathVariable("roleId") Long roleId) {
        if (roleId == null || roleId <= 0) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "请求参数错误");
        }
        Role role = roleService.getById(roleId);
        // 查询角色对应的菜单
        List<Long> menuIdList = roleMenuService.queryMenuIdList(roleId);
        role.setMenuIdList(menuIdList);
        return ResultUtils.success(role);
    }

    /**
     * 保存角色
     */
    @SysLog("保存角色")
    @PostMapping("/save")
    @RequiresPermissions("sys:role:save")
    public BaseResponse save(@RequestBody Role role) {
        User user = getUser();
        role.setCreator(user.getUsername());
        roleService.saveRole(role);
        return ResultUtils.success(true);
    }

    /**
     * 修改角色
     */
    @SysLog("修改角色")
    @PostMapping("/update")
    @RequiresPermissions("sys:role:update")
    public BaseResponse update(@RequestBody Role role) {
        role.setUpdater(getUser().getUsername());
        roleService.update(role);
        return ResultUtils.success(true);
    }

    /**
     * 删除角色
     */
    @SysLog("删除角色")
    @PostMapping("/delete/{roleId}")
    @RequiresPermissions("sys:role:delete")
    public BaseResponse delete(@PathVariable("roleId") Long roleId) {
        if (roleId == null || roleId <= 0) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "请求参数错误");
        }
        Role role = roleService.getById(roleId);
        if (role == null) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "查询失败");
        }
        role.setUpdater(getUser().getUsername());
        roleService.updateById(role);
        roleService.delete(roleId);
        return ResultUtils.success(true);
    }
}
