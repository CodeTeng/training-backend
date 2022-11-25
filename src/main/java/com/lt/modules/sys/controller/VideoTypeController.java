package com.lt.modules.sys.controller;

import com.lt.common.BaseResponse;
import com.lt.common.ErrorCode;
import com.lt.common.annotation.SysLog;
import com.lt.common.utils.PageUtils;
import com.lt.common.utils.ResultUtils;
import com.lt.modules.sys.model.entity.VideoType;
import com.lt.modules.sys.service.VideoTypeService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @description: 视频类别管理
 * @author: ~Teng~
 * @date: 2022/11/21 15:28
 */
@RestController
@RequestMapping("/sys/videoType")
public class VideoTypeController extends AbstractController {

    @Autowired
    private VideoTypeService videoTypeService;

    /**
     * 分页查询
     */
    @GetMapping("/list")
    @RequiresPermissions("sys:video:list")
    public BaseResponse list(@RequestParam Map<String, Object> params) {
        PageUtils page = videoTypeService.queryPage(params);
        return ResultUtils.success(page);
    }

    /**
     * 所有视频类别信息
     */
    @GetMapping("/select")
    @RequiresPermissions("sys:video:select")
    public BaseResponse select() {
        List<VideoType> list = videoTypeService.list();
        return ResultUtils.success(list);
    }

    /**
     * 单个视频类别信息
     */
    @GetMapping("/info/{id}")
    @RequiresPermissions("sys:video:info")
    public BaseResponse info(@PathVariable("id") Long id) {
        if (id == null || id <= 0) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "请求参数错误");
        }
        VideoType videoType = videoTypeService.getById(id);
        if (videoType == null) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "未查询到该机构类型");
        }
        return ResultUtils.success(videoType);
    }

    @PostMapping("/save")
    @SysLog("添加视频类型")
    @RequiresPermissions("sys:video:save")
    public BaseResponse save(@RequestBody @Validated VideoType videoType) {
        videoType.setCreator(getUser().getUsername());
        boolean flag = videoTypeService.save(videoType);
        if (!flag) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "添加失败");
        }
        return ResultUtils.success(flag);
    }

    @PostMapping("/update")
    @SysLog("修改视频类型")
    @RequiresPermissions("sys:video:update")
    public BaseResponse update(@RequestBody @Validated VideoType videoType) {
        videoType.setUpdater(getUser().getUsername());
        boolean flag = videoTypeService.updateById(videoType);
        if (!flag) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "修改失败");
        }
        return ResultUtils.success(flag);
    }

    @SysLog("删除视频类型")
    @PostMapping("/delete/{id}")
    @RequiresPermissions("sys:video:delete")
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse delete(@PathVariable("id") Long id) {
        if (id == null || id <= 0) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "请求参数错误");
        }
        VideoType videoType = videoTypeService.getById(id);
        videoType.setUpdater(getUser().getUsername());
        videoTypeService.updateById(videoType);
        boolean flag = videoTypeService.removeById(videoType);
        if (!flag) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "删除失败");
        }
        return ResultUtils.success(flag);
    }
}
