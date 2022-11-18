package com.lt.modules.sys.service;

import com.lt.common.utils.PageUtils;
import com.lt.modules.sys.model.entity.Config;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * @author teng
 * @description 针对表【config(系统配置信息表)】的数据库操作Service
 * @createDate 2022-11-18 21:32:58
 */
public interface ConfigService extends IService<Config> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 保存配置信息
     */
    void saveConfig(Config config);

    /**
     * 更新配置信息
     */
    void update(Config config);

    /**
     * 删除配置
     */
    void delete(Long configId);
}
