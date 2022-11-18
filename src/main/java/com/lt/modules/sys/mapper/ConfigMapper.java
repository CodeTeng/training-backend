package com.lt.modules.sys.mapper;

import com.lt.modules.sys.model.entity.Config;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * @author teng
 * @description 针对表【config(系统配置信息表)】的数据库操作Mapper
 * @createDate 2022-11-18 21:32:58
 * @Entity com.lt.modules.sys.model.entity.Config
 */
@Repository
public interface ConfigMapper extends BaseMapper<Config> {

}




