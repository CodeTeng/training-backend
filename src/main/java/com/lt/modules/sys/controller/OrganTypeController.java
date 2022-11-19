package com.lt.modules.sys.controller;

import com.lt.common.BaseResponse;
import com.lt.common.ErrorCode;
import com.lt.common.annotation.SysLog;
import com.lt.common.utils.PageUtils;
import com.lt.common.utils.ResultUtils;
import com.lt.modules.sys.model.entity.OrganType;
import com.lt.modules.sys.service.OrganTypeService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @description: 培训类型管理
 * @author: ~Teng~
 * @date: 2022/11/19 19:53
 */
@RestController
@RequestMapping("/sys/organType")
public class OrganTypeController extends AbstractController {

    @Autowired
    private OrganTypeService organTypeService;

    /**
     * 分页查询
     */
    @RequiresPermissions("sys:organ:list")
    @GetMapping("/list")
    public BaseResponse list(@RequestParam Map<String, Object> params) {
        PageUtils page = organTypeService.queryPage(params);
        return ResultUtils.success(page);
    }

    /**
     * 所有机构类型信息
     */
    @GetMapping("/select")
    @RequiresPermissions("sys:organ:select")
    public BaseResponse select() {
        List<OrganType> organTypeList = organTypeService.list();
        return ResultUtils.success(organTypeList);
    }

    /**
     * 单个机构类型信息
     */
    @GetMapping("/info/{id}")
    @RequiresPermissions("sys:organ:info")
    public BaseResponse info(@PathVariable("id") Long id) {
        if (id == null || id <= 0) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "请求参数错误");
        }
        OrganType organType = organTypeService.getById(id);
        if (organType == null) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "未查询到该机构类型");
        }
        return ResultUtils.success(organType);
    }

    /**
     * 添加机构类型
     */
    @PostMapping("/save")
    @SysLog("添加机构类型")
    @RequiresPermissions("sys:organ:save")
    public BaseResponse save(@RequestBody OrganType organType) {
        organType.setCreator(getUser().getUsername());
        boolean flag = organTypeService.save(organType);
        if (!flag) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "添加失败");
        }
        return ResultUtils.success(flag);
    }

    /**
     * 修改机构类型
     */
    @SysLog("修改机构类型信息")
    @PostMapping("/update")
    @RequiresPermissions("sys:organ:update")
    public BaseResponse update(@RequestBody OrganType organType) {
        organType.setUpdater(getUser().getUsername());
        boolean flag = organTypeService.updateById(organType);
        if (!flag) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "修改失败");
        }
        return ResultUtils.success(flag);
    }

    @SysLog("删除机构类型")
    @PostMapping("/delete/{id}")
    @RequiresPermissions("sys:organ:delete")
    public BaseResponse delete(@PathVariable("id") Long id) {
        if (id == null || id <= 0) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "请求参数错误");
        }
        OrganType organType = organTypeService.getById(id);
        organType.setUpdater(getUser().getUsername());
        boolean flag = organTypeService.removeById(organType);
        if (!flag) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "删除失败");
        }
        return ResultUtils.success(flag);
    }
}
