package com.lt.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.common.utils.PageUtils;
import com.lt.common.utils.Query;
import com.lt.modules.sys.model.entity.OrganType;
import com.lt.modules.sys.service.OrganTypeService;
import com.lt.modules.sys.mapper.OrganTypeMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author teng
 * @description 针对表【organ_type(机构类型表)】的数据库操作Service实现
 * @createDate 2022-11-19 14:50:36
 */
@Service
public class OrganTypeServiceImpl extends ServiceImpl<OrganTypeMapper, OrganType>
        implements OrganTypeService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String typeName = (String) params.get("typeName");
        IPage<OrganType> page = this.page(
                new Query<OrganType>().getPage(params),
                new QueryWrapper<OrganType>()
                        .like(StringUtils.isNotBlank(typeName), "typeName", typeName)
                        .eq("isDelete", 0)
        );
        return new PageUtils(page);
    }
}




