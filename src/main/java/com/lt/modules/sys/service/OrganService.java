package com.lt.modules.sys.service;

import com.lt.common.utils.PageUtils;
import com.lt.modules.sys.model.entity.Organ;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lt.modules.sys.model.vo.organ.OrganVO;

import java.util.List;
import java.util.Map;

/**
* @author teng
* @description 针对表【organ(培训机构表)】的数据库操作Service
* @createDate 2022-11-19 14:50:36
*/
public interface OrganService extends IService<Organ> {

    PageUtils queryPage(Map<String, Object> params);

    List<OrganVO> getOrganInfo(Long organTypeId);
}
