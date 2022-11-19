package com.lt.modules.sys.service;

import com.lt.common.utils.PageUtils;
import com.lt.modules.sys.model.entity.OrganType;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * @author teng
 * @description 针对表【organ_type(机构类型表)】的数据库操作Service
 * @createDate 2022-11-19 14:50:36
 */
public interface OrganTypeService extends IService<OrganType> {

    PageUtils queryPage(Map<String, Object> params);
}
