package com.lt.modules.sys.controller;

import com.lt.common.BaseResponse;
import com.lt.common.ErrorCode;
import com.lt.common.annotation.SysLog;
import com.lt.common.utils.PageUtils;
import com.lt.common.utils.ResultUtils;
import com.lt.modules.sys.model.entity.Notice;
import com.lt.modules.sys.service.NoticeService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @description: 公告管理
 * @author: ~Teng~
 * @date: 2022/11/20 0:09
 */
@RestController
@RequestMapping("/sys/notice")
public class NoticeController extends AbstractController {

    @Autowired
    private NoticeService noticeService;

    /**
     * 分页查询
     */
    @RequestMapping("/list")
    @RequiresPermissions("sys:notice:list")
    public BaseResponse list(@RequestParam Map<String, Object> map) {
        PageUtils page = noticeService.queryPage(map);
        return ResultUtils.success(page);
    }

    /**
     * 所有公告信息
     */
    @GetMapping("/select")
    @RequiresPermissions("sys:notice:select")
    public BaseResponse select() {
        List<Notice> list = noticeService.list();
        return ResultUtils.success(list);
    }

    /**
     * 单个公告信息
     */
    @GetMapping("/info/{id}")
    @RequiresPermissions("sys:notice:info")
    public BaseResponse info(@PathVariable("id") Long id) {
        if (id == null || id <= 0) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "请求参数错误");
        }
        Notice notice = noticeService.getById(id);
        if (notice == null) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "查询失败");
        }
        return ResultUtils.success(notice);
    }

    @PostMapping("/save")
    @SysLog("添加公告")
    @RequiresPermissions("sys:notice:save")
    public BaseResponse save(@RequestBody @Validated Notice notice) {
        notice.setCreator(getUser().getUsername());
        boolean flag = noticeService.save(notice);
        if (!flag) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "添加失败");
        }
        return ResultUtils.success(flag);
    }

    @SysLog("修改公告")
    @PostMapping("/update")
    @RequiresPermissions("sys:notice:update")
    public BaseResponse update(@RequestBody @Validated Notice notice) {
        notice.setUpdater(getUser().getUsername());
        boolean flag = noticeService.updateById(notice);
        if (!flag) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "修改失败");
        }
        return ResultUtils.success(flag);
    }

    @SysLog("删除公告")
    @PostMapping("/delete/{id}")
    @RequiresPermissions("sys:notice:delete")
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse delete(@PathVariable("id") Long id) {
        if (id == null || id <= 0) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "请求参数错误");
        }
        Notice notice = noticeService.getById(id);
        if (notice == null) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "查询失败");
        }
        notice.setUpdater(getUser().getUsername());
        noticeService.updateById(notice);
        boolean flag = noticeService.removeById(notice);
        if (!flag) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "删除失败");
        }
        return ResultUtils.success(flag);
    }
}
