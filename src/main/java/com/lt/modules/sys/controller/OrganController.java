package com.lt.modules.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lt.common.BaseResponse;
import com.lt.common.ErrorCode;
import com.lt.common.annotation.SysLog;
import com.lt.common.utils.PageUtils;
import com.lt.common.utils.ResultUtils;
import com.lt.modules.sys.model.dto.organ.OrganAddRequest;
import com.lt.modules.sys.model.entity.Organ;
import com.lt.modules.sys.model.entity.OrganType;
import com.lt.modules.sys.model.vo.OrganVO;
import com.lt.modules.sys.service.OrganService;
import com.lt.modules.sys.service.OrganTypeService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @description: 机构管理
 * @author: ~Teng~
 * @date: 2022/11/19 14:52
 */
@RestController
@RequestMapping("/sys/organ")
public class OrganController extends AbstractController {

    @Autowired
    private OrganService organService;

    @Autowired
    private OrganTypeService organTypeService;

    /**
     * 分页查询
     */
    @RequiresPermissions("sys:organ:list")
    @GetMapping("/list")
    public BaseResponse list(@RequestParam Map<String, Object> params) {
        PageUtils page = organService.queryPage(params);
        return ResultUtils.success(page);
    }


    /**
     * 根据机构类型id 获取该类型下的所有机构
     */
    @GetMapping("/list/{organTypeId}")
    @RequiresPermissions("sys:organ:info")
    public BaseResponse getOrganInfo(@PathVariable("organTypeId") Long organTypeId) {
        if (organTypeId == null || organTypeId <= 0) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "请求参数错误");
        }
        List<OrganVO> organInfo = organService.getOrganInfo(organTypeId);
        return ResultUtils.success(organInfo);
    }

    /**
     * 所有机构信息
     */
    @GetMapping("/select")
    @RequiresPermissions("sys:organ:select")
    public BaseResponse select() {
        List<Organ> organList = organService.list();
        List<OrganVO> list = organList.stream().map(organ -> {
            OrganVO organVO = new OrganVO();
            BeanUtils.copyProperties(organ, organVO);
            Long organTypeId = organ.getOrganTypeId();
            OrganType organType = organTypeService.getById(organTypeId);
            organVO.setTypeName(organType.getTypeName());
            return organVO;
        }).toList();
        return ResultUtils.success(list);
    }

    /**
     * 单个机构信息
     */
    @GetMapping("/info/{organId}")
    @RequiresPermissions("sys:organ:info")
    public BaseResponse info(@PathVariable("organId") Long organId) {
        if (organId == null || organId <= 0) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "请求参数错误");
        }
        Organ organ = organService.getById(organId);
        if (organ == null) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "查询失败");
        }
        OrganVO organVO = new OrganVO();
        BeanUtils.copyProperties(organ, organVO);
        OrganType organType = organTypeService.getById(organ.getOrganTypeId());
        organVO.setTypeName(organType.getTypeName());
        return ResultUtils.success(organVO);
    }

    /**
     * 添加机构
     */
    @PostMapping("/save")
    @SysLog("添加机构")
    @RequiresPermissions("sys:organ:save")
    public BaseResponse save(@RequestBody OrganAddRequest organAddRequest) {
        String typeName = organAddRequest.getTypeName();
        QueryWrapper<OrganType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("typeName", typeName);
        OrganType organType = organTypeService.getOne(queryWrapper);
        Organ organ = new Organ();
        BeanUtils.copyProperties(organAddRequest, organ);
        organ.setCreator(getUser().getUsername());
        organ.setOrganTypeId(organType.getId());
        boolean flag = organService.save(organ);
        if (!flag) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "添加失败");
        }
        return ResultUtils.success(flag);
    }

    /**
     * 修改机构
     */
    @SysLog("修改机构信息")
    @PostMapping("/update")
    @RequiresPermissions("sys:organ:update")
    public BaseResponse update(@RequestBody Organ organ) {
        organ.setUpdater(getUser().getUsername());
        boolean flag = organService.updateById(organ);
        if (!flag) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "修改失败");
        }
        return ResultUtils.success(flag);
    }

    @SysLog("删除机构")
    @PostMapping("/delete/{organId}")
    @RequiresPermissions("sys:organ:delete")
    public BaseResponse delete(@PathVariable("organId") Long organId) {
        if (organId == null || organId <= 0) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "请求参数错误");
        }
        Organ organ = organService.getById(organId);
        if (organ == null) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "未查询到该机构");
        }
        organ.setUpdater(getUser().getUsername());
        boolean flag = organService.removeById(organ);
        if (!flag) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "删除失败");
        }
        return ResultUtils.success(flag);
    }
}
