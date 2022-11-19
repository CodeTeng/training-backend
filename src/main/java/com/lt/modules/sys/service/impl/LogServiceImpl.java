package com.lt.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.common.utils.PageUtils;
import com.lt.common.utils.Query;
import com.lt.modules.sys.model.entity.Log;
import com.lt.modules.sys.service.LogService;
import com.lt.modules.sys.mapper.LogMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author teng
 * @description 针对表【log(日志表)】的数据库操作Service实现
 * @createDate 2022-11-17 14:39:11
 */
@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, Log>
        implements LogService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String username = (String) params.get("username");
        IPage<Log> page = this.page(
                new Query<Log>().getPage(params),
                new QueryWrapper<Log>()
                        .like(StringUtils.isNotBlank(username), "username", username)
        );
        return new PageUtils(page);
    }
}




