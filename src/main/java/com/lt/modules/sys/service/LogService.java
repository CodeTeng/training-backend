package com.lt.modules.sys.service;

import com.lt.common.utils.PageUtils;
import com.lt.modules.sys.model.entity.Log;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
* @author teng
* @description 针对表【log(日志表)】的数据库操作Service
* @createDate 2022-11-17 14:39:11
*/
public interface LogService extends IService<Log> {

    PageUtils queryPage(Map<String, Object> params);
}
