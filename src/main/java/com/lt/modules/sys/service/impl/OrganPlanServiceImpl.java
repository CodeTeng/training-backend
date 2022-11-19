package com.lt.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.common.ErrorCode;
import com.lt.common.exception.BusinessException;
import com.lt.common.utils.PageUtils;
import com.lt.common.utils.Query;
import com.lt.modules.sys.mapper.OrganMapper;
import com.lt.modules.sys.model.entity.Organ;
import com.lt.modules.sys.model.entity.OrganPlan;
import com.lt.modules.sys.model.vo.OrganPlanVO;
import com.lt.modules.sys.service.OrganPlanService;
import com.lt.modules.sys.mapper.OrganPlanMapper;
import com.tencentcloudapi.billing.v20180709.models.BusinessSummaryOverviewItem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author teng
 * @description 针对表【organ_plan(培训机构计划表)】的数据库操作Service实现
 * @createDate 2022-11-19 14:50:36
 */
@Service
public class OrganPlanServiceImpl extends ServiceImpl<OrganPlanMapper, OrganPlan>
        implements OrganPlanService {

    @Autowired
    private OrganMapper organMapper;

    @Autowired
    private OrganPlanMapper organPlanMapper;

    @Override
    public PageUtils queryPage(Integer lowTime, Integer highTime) {
        // 培训周期 最少几天 最多几天
        if (lowTime != null && highTime != null && (lowTime < 0 || highTime < 0 || highTime < lowTime)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数错误");
        }
        IPage<OrganPlan> page = new Page<>(1, 10);
        QueryWrapper<OrganPlan> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge(lowTime != null, "trainPeriod", lowTime)
                .le(highTime != null, "trainPeriod", highTime)
                .eq("isDelete", 0);
        this.page(page, queryWrapper);
        List<OrganPlan> records = page.getRecords();
        IPage<OrganPlanVO> resPage = new Page<>();
        BeanUtils.copyProperties(page, resPage);
        List<OrganPlanVO> organPlanVOList = records.stream().map(organPlan -> {
            OrganPlanVO organPlanVO = new OrganPlanVO();
            BeanUtils.copyProperties(organPlan, organPlanVO);
            Long organId = organPlan.getOrganId();
            Organ organ = organMapper.selectById(organId);
            organPlanVO.setName(organ.getName());
            return organPlanVO;
        }).toList();
        resPage.setRecords(organPlanVOList);
        return new PageUtils(resPage);
    }

    @Override
    public List<OrganPlanVO> getOrganPlanInfo(Long organId) {
        QueryWrapper<OrganPlan> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("organId", organId);
        List<OrganPlan> organPlanList = organPlanMapper.selectList(queryWrapper);
        return organPlanList.stream().map(organPlan -> {
            OrganPlanVO organPlanVO = new OrganPlanVO();
            BeanUtils.copyProperties(organPlan, organPlanVO);
            Long id = organPlan.getOrganId();
            Organ organ = organMapper.selectById(id);
            organPlanVO.setName(organ.getName());
            return organPlanVO;
        }).toList();
    }
}




