package com.lt.modules.sys.controller;

import com.lt.common.BaseResponse;
import com.lt.common.utils.PageUtils;
import com.lt.common.utils.ResultUtils;
import com.lt.modules.sys.model.entity.Log;
import com.lt.modules.sys.service.LogService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


/**
 * @description: 系统日志控制器
 * @author: ~Teng~
 * @date: 2022/11/17 14:41
 */
@RestController
@RequestMapping("/sys/log")
public class LogController {

    @Autowired
    private LogService logService;

    /**
     * 分页查询系统日志
     */
    @GetMapping("/list")
    @RequiresPermissions("sys:log:list")
    public BaseResponse list(@RequestParam Map<String, Object> params) {
        PageUtils page = logService.queryPage(params);
        return ResultUtils.success(page.getList());
    }
}
