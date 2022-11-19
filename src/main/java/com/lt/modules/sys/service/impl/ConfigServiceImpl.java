package com.lt.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.common.utils.PageUtils;
import com.lt.common.utils.Query;
import com.lt.modules.sys.model.entity.Config;
import com.lt.modules.sys.reids.ConfigRedis;
import com.lt.modules.sys.service.ConfigService;
import com.lt.modules.sys.mapper.ConfigMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * @author teng
 * @description 针对表【config(系统配置信息表)】的数据库操作Service实现
 * @createDate 2022-11-18 21:32:58
 */
@Service
public class ConfigServiceImpl extends ServiceImpl<ConfigMapper, Config>
        implements ConfigService {

    @Autowired
    private ConfigRedis configRedis;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String paramKey = (String) params.get("paramKey");
        IPage<Config> page = this.page(
                new Query<Config>().getPage(params),
                new QueryWrapper<Config>()
                        .like(StringUtils.isNotBlank(paramKey), "paramKey", paramKey)
                        .eq("status", 1)
                        .eq("isDelete", 0)
        );
        return new PageUtils(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveConfig(Config config) {
        this.save(config);
        configRedis.saveOrUpdate(config);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Config config) {
        this.updateById(config);
        configRedis.saveOrUpdate(config);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long configId) {
        Config config = this.getById(configId);
        configRedis.delete(config.getParamKey());
        this.removeById(configId);
    }
}




