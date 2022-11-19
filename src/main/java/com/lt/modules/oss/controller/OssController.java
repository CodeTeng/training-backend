package com.lt.modules.oss.controller;

import com.lt.common.BaseResponse;
import com.lt.common.annotation.SysLog;
import com.lt.common.utils.ResultUtils;
import com.lt.modules.oss.service.OssService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @description: 图片上传
 * @author: ~Teng~
 * @date: 2022/11/19 9:42
 */
@RestController
@RequestMapping("/sys/oss")
public class OssController {

    @Autowired
    private OssService ossService;

    /**
     * 上传图片
     */
    @PostMapping("/upload")
    @SysLog("上传图片")
    @RequiresPermissions("sys:oss:all")
    public BaseResponse uploadOssFile(MultipartFile file) {
        // 获取上传文件 MultipartFile
        // 返回上传到oss的路径
        String url = ossService.uploadFileAvatar(file);
        return ResultUtils.success(url);
    }
}
