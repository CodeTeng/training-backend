package com.lt.modules.sys.controller;

import com.lt.common.BaseResponse;
import com.lt.common.ErrorCode;
import com.lt.common.annotation.SysLog;
import com.lt.common.utils.DateUtils;
import com.lt.common.utils.PageUtils;
import com.lt.common.utils.ResultUtils;
import com.lt.modules.sys.model.entity.Organ;
import com.lt.modules.sys.model.entity.OrganPlan;
import com.lt.modules.sys.model.vo.organ.OrganPlanVO;
import com.lt.modules.sys.service.OrganPlanService;
import com.lt.modules.sys.service.OrganService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @description: 培训计划管理
 * @author: ~Teng~
 * @date: 2022/11/19 20:24
 */
@RestController
@RequestMapping("/sys/organPlan")
public class OrganPlanController extends AbstractController {

    @Autowired
    private OrganPlanService organPlanService;

    @Autowired
    private OrganService organService;

    /**
     * 分页查询
     */
    @RequiresPermissions("sys:organ:list")
    @GetMapping("/list")
    public BaseResponse list(@RequestParam(required = false) Integer lowTime,
                             @RequestParam(required = false) Integer highTime) {
        PageUtils page = organPlanService.queryPage(lowTime, highTime);
        return ResultUtils.success(page);
    }

    /**
     * 根据机构id查看该机构的所有培训计划
     */
    @GetMapping("/list/{organId}")
    @RequiresPermissions("sys:organ:info")
    public BaseResponse getOrganPlanInfo(@PathVariable("organId") Long organId) {
        if (organId == null || organId <= 0) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "请求参数错误");
        }
        List<OrganPlanVO> organPlanVOList = organPlanService.getOrganPlanInfo(organId);
        return ResultUtils.success(organPlanVOList);
    }

    /**
     * 所有培训计划信息
     */
    @GetMapping("/select")
    @RequiresPermissions("sys:organ:select")
    public BaseResponse select() {
        List<OrganPlan> organPlanList = organPlanService.list();
        List<OrganPlanVO> organPlanVOList = organPlanList.stream().map(organPlan -> {
            OrganPlanVO organPlanVO = new OrganPlanVO();
            BeanUtils.copyProperties(organPlan, organPlanVO);
            Long organId = organPlan.getOrganId();
            Organ organ = organService.getById(organId);
            organPlanVO.setName(organ.getName());
            return organPlanVO;
        }).toList();
        return ResultUtils.success(organPlanVOList);
    }

    /**
     * 单个培训计划信息
     */
    @GetMapping("/info/{id}")
    @RequiresPermissions("sys:organ:info")
    public BaseResponse info(@PathVariable("id") Long id) {
        if (id == null || id <= 0) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "请求参数错误");
        }
        OrganPlan organPlan = organPlanService.getById(id);
        if (organPlan == null) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "未查询到该培训计划");
        }
        OrganPlanVO organPlanVO = new OrganPlanVO();
        BeanUtils.copyProperties(organPlan, organPlanVO);
        Organ organ = organService.getById(organPlan.getOrganId());
        organPlanVO.setName(organ.getName());
        return ResultUtils.success(organPlanVO);
    }

    @PostMapping("/save")
    @SysLog("添加培训计划")
    @RequiresPermissions("sys:organ:save")
    public BaseResponse save(@RequestBody @Validated OrganPlan organPlan) {
        if (organPlan == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "参数错误");
        }
        Date endTime = organPlan.getEndTime();
        Date startTime = organPlan.getStartTime();
        Integer trainPeriod = organPlan.getTrainPeriod();
        if (endTime == null) {
            endTime = DateUtils.addDateDays(startTime, trainPeriod);
            organPlan.setEndTime(endTime);
        }
        organPlan.setCreator(getUser().getUsername());
        boolean flag = organPlanService.save(organPlan);
        if (!flag) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "添加失败");
        }
        return ResultUtils.success(flag);
    }

    @PostMapping("/update")
    @SysLog("修改培训计划")
    @RequiresPermissions("sys:organ:update")
    public BaseResponse update(@RequestBody @Validated OrganPlan organPlan) {
        if (organPlan == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "参数错误");
        }
        organPlan.setUpdater(getUser().getUsername());
        boolean flag = organPlanService.updateById(organPlan);
        if (!flag) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "修改失败");
        }
        return ResultUtils.success(flag);
    }

    @SysLog("删除培训计划")
    @PostMapping("/delete/{id}")
    @RequiresPermissions("sys:organ:delete")
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse delete(@PathVariable("id") Long id) {
        if (id == null || id <= 0) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "参数错误");
        }
        OrganPlan organPlan = organPlanService.getById(id);
        if (organPlan == null) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "未查询到该培训计划");
        }
        organPlan.setUpdater(getUser().getUsername());
        organPlanService.updateById(organPlan);
        boolean flag = organPlanService.removeById(organPlan);
        if (!flag) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "删除失败");
        }
        return ResultUtils.success(flag);
    }
}
