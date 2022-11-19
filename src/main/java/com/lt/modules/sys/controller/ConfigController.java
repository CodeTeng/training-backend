package com.lt.modules.sys.controller;

import com.lt.common.BaseResponse;
import com.lt.common.ErrorCode;
import com.lt.common.annotation.SysLog;
import com.lt.common.utils.PageUtils;
import com.lt.common.utils.ResultUtils;
import com.lt.modules.sys.model.entity.Config;
import com.lt.modules.sys.service.ConfigService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @description: 系统配置
 * @author: ~Teng~
 * @date: 2022/11/18 21:34
 */
@RestController
@RequestMapping("/sys/config")
public class ConfigController extends AbstractController {

    @Autowired
    private ConfigService configService;

    /**
     * 分页查询
     */
    @GetMapping("/list")
    @RequiresPermissions("sys:config:list")
    public BaseResponse list(@RequestParam Map<String, Object> params) {
        PageUtils page = configService.queryPage(params);
        return ResultUtils.success(page);
    }

    /**
     * 配置信息
     */
    @GetMapping("/info/{id}")
    @RequiresPermissions("sys:config:info")
    public BaseResponse info(@PathVariable("id") Long id) {
        if (id == null || id <= 0) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "请求参数错误");
        }
        Config config = configService.getById(id);
        return ResultUtils.success(config);
    }

    /**
     * 保存配置
     */
    @SysLog("保存配置")
    @PostMapping("/save")
    @RequiresPermissions("sys:config:save")
    public BaseResponse save(@RequestBody Config config) {
        config.setCreator(getUser().getUsername());
        configService.saveConfig(config);
        return ResultUtils.success(true);
    }

    /**
     * 修改配置
     */
    @SysLog("修改配置")
    @PostMapping("/update")
    @RequiresPermissions("sys:config:update")
    public BaseResponse update(@RequestBody Config config) {
        config.setUpdater(getUser().getUsername());
        configService.update(config);
        return ResultUtils.success(true);
    }

    /**
     * 删除配置
     */
    @SysLog("删除配置")
    @PostMapping("/delete/{configId}")
    @RequiresPermissions("sys:config:delete")
    public BaseResponse delete(@PathVariable("configId") Long configId) {
        if (configId == null || configId <= 0) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "请求参数错误");
        }
        Config config = configService.getById(configId);
        config.setUpdater(getUser().getUsername());
        configService.updateById(config);
        configService.delete(configId);
        return ResultUtils.success(true);
    }
}
