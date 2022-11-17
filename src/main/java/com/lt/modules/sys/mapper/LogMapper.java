package com.lt.modules.sys.mapper;

import com.lt.modules.sys.model.entity.Log;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * @author teng
 * @description 针对表【log(日志表)】的数据库操作Mapper
 * @createDate 2022-11-17 14:39:11
 * @Entity com.lt.modules.sys.model.entity.Log
 */
@Repository
public interface LogMapper extends BaseMapper<Log> {

}




