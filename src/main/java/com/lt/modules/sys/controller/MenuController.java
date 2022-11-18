package com.lt.modules.sys.controller;

import com.lt.common.BaseResponse;
import com.lt.common.ErrorCode;
import com.lt.common.annotation.SysLog;
import com.lt.common.exception.BusinessException;
import com.lt.common.utils.ResultUtils;
import com.lt.constant.Constant;
import com.lt.modules.sys.model.entity.Menu;
import com.lt.modules.sys.service.MenuService;
import com.lt.modules.sys.service.ShiroService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @description: 菜单管理
 * @author: ~Teng~
 * @date: 2022/11/18 19:08
 */
@RestController
@RequestMapping("/sys/menu")
public class MenuController extends AbstractController {

    @Autowired
    private MenuService menuService;

    @Autowired
    private ShiroService shiroService;

    /**
     * 导航菜单
     */
    @GetMapping("/nav")
    public BaseResponse nav() {
        List<Menu> menuList = menuService.getUserMenuList(getUserId());
        Set<String> permissions = shiroService.getUserPermissions(getUserId());
        Map<String, Object> map = new HashMap<>(2);
        map.put("menuList", menuList);
        map.put("permissions", permissions);
        return ResultUtils.success(map);
    }

    /**
     * 所有菜单列表
     */
    @GetMapping("/list")
    @RequiresPermissions("sys:menu:list")
    public BaseResponse<List<Menu>> list() {
        List<Menu> menuList = menuService.list();
        // 查询完成 对此list直接排序
        Collections.sort(menuList);
        HashMap<Long, Menu> menuMap = new HashMap<>(12);
        for (Menu menu : menuList) {
            menuMap.put(menu.getId(), menu);
        }
        for (Menu menu : menuList) {
            Menu parent = menuMap.get(menu.getParentId());
            if (Objects.nonNull(parent)) {
                menu.setParentName(parent.getName());
            }
        }
        return ResultUtils.success(menuList);
    }

    /**
     * 菜单信息
     */
    @GetMapping("/info/{menuId}")
    @RequiresPermissions("sys:menu:info")
    public BaseResponse info(@PathVariable("menuId") Long menuId) {
        if (menuId == null || menuId <= 0) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "请求参数错误");
        }
        Menu menu = menuService.getById(menuId);
        return ResultUtils.success(menu);
    }

    /**
     * 保存
     */
    @SysLog("保存菜单")
    @PostMapping("/save")
    @RequiresPermissions("sys:menu:save")
    public BaseResponse save(@RequestBody Menu menu) {
        // 数据校验
        verifyForm(menu);
        menu.setCreator(getUser().getUsername());
        menuService.save(menu);
        return ResultUtils.success(true);
    }

    /**
     * 修改
     */
    @SysLog("修改菜单")
    @PostMapping("/update")
    @RequiresPermissions("sys:menu:update")
    public BaseResponse update(@RequestBody Menu menu) {
        // 数据校验
        verifyForm(menu);
        menu.setUpdater(getUser().getUsername());
        menuService.updateById(menu);
        return ResultUtils.success(true);
    }

    /**
     * 删除
     */
    @SysLog("删除菜单")
    @PostMapping("/delete/{menuId}")
    @RequiresPermissions("sys:menu:delete")
    public BaseResponse delete(@PathVariable("menuId") Long menuId) {
        if (menuId == null || menuId <= 0) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "请求参数错误");
        }
        // TODO 待确定
        if (menuId <= 4) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "系统菜单，不能删除");
        }
        // 判断是否有子菜单或按钮
        List<Menu> menuList = menuService.queryListParentId(menuId);
        if (menuList.size() > 0) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "请先删除子菜单或按钮");
        }
        Menu menu = menuService.getById(menuId);
        menu.setUpdater(getUser().getUsername());
        menuService.updateById(menu);
        menuService.delete(menuId);
        return ResultUtils.success(true);
    }

    /**
     * 验证参数是否正确
     */
    private void verifyForm(Menu menu) {
        // 菜单
        if (menu.getType() == Constant.MenuType.MENU.getValue()) {
            if (StringUtils.isBlank(menu.getUrl())) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "菜单URL不能为空");
            }
        }
        // 上级菜单类型
        int parentType = Constant.MenuType.CATALOG.getValue();
        if (menu.getParentId() != 0) {
            Menu parentMenu = menuService.getById(menu.getParentId());
            parentType = parentMenu.getType();
        }
        // 目录、菜单
        if (menu.getType() == Constant.MenuType.CATALOG.getValue() ||
                menu.getType() == Constant.MenuType.MENU.getValue()) {
            if (parentType != Constant.MenuType.CATALOG.getValue()) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "上级菜单只能为目录类型");
            }
            return;
        }
        // 按钮
        if (menu.getType() == Constant.MenuType.BUTTON.getValue()) {
            if (parentType != Constant.MenuType.MENU.getValue()) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "上级菜单只能为菜单类型");
            }
        }
    }

    /**
     * 选择菜单(添加、修改菜单)
     */
//    @GetMapping("/select")
//    @RequiresPermissions("sys:menu:select")
//    public BaseResponse select() {
//        // 查询列表数据
//        List<Menu> menuList = menuService.queryNotButtonList();
//        // 添加顶级菜单
//        Menu root = new Menu();
//        root.setId(0L);
//        root.setName("一级菜单");
//        root.setParentId(0L);
//        root.setOpen(true);
//        menuList.add(root);
//        return ResultUtils.success(menuList);
//    }

}
