package com.lt.modules.sys.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lt.common.utils.PageUtils;
import com.lt.modules.sys.model.entity.OrganPlan;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lt.modules.sys.model.vo.organ.OrganPlanVO;

import java.util.List;

/**
 * @author teng
 * @description 针对表【organ_plan(培训机构计划表)】的数据库操作Service
 * @createDate 2022-11-19 14:50:36
 */
public interface OrganPlanService extends IService<OrganPlan> {

    PageUtils queryPage(Integer lowTime, Integer highTime);

    /**
     * 根据机构id查看该机构的所有培训计划
     */
    List<OrganPlanVO> getOrganPlanInfo(Long organId);

//    Page<OrganPlanVO> getAllOrganPlanByPage(Integer pageNo, Integer pageSize, Integer lowTime, Integer highTime, String startTime, String endTime);
}
