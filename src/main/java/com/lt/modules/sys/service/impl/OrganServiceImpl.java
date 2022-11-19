package com.lt.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.common.utils.PageUtils;
import com.lt.common.utils.Query;
import com.lt.modules.sys.mapper.OrganTypeMapper;
import com.lt.modules.sys.model.entity.Config;
import com.lt.modules.sys.model.entity.Organ;
import com.lt.modules.sys.model.entity.OrganType;
import com.lt.modules.sys.model.vo.OrganVO;
import com.lt.modules.sys.service.OrganService;
import com.lt.modules.sys.mapper.OrganMapper;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author teng
 * @description 针对表【organ(培训机构表)】的数据库操作Service实现
 * @createDate 2022-11-19 14:50:36
 */
@Service
public class OrganServiceImpl extends ServiceImpl<OrganMapper, Organ>
        implements OrganService {

    @Autowired
    private OrganMapper organMapper;

    @Autowired
    private OrganTypeMapper organTypeMapper;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String name = (String) params.get("name");
        IPage<Organ> page = this.page(
                new Query<Organ>().getPage(params),
                new QueryWrapper<Organ>()
                        .like(StringUtils.isNotBlank(name), "name", name)
                        .eq("isDelete", 0));
        List<Organ> records = page.getRecords();
        IPage<OrganVO> resPage = new Page<>();
        BeanUtils.copyProperties(page, resPage);
        List<OrganVO> organVOList = records.stream().map(item -> {
            OrganVO organVO = new OrganVO();
            BeanUtils.copyProperties(item, organVO);
            Long organTypeId = item.getOrganTypeId();
            OrganType organType = organTypeMapper.selectById(organTypeId);
            organVO.setTypeName(organType.getTypeName());
            return organVO;
        }).toList();
        resPage.setRecords(organVOList);
        return new PageUtils(resPage);
    }

    @Override
    public List<OrganVO> getOrganInfo(Long organTypeId) {
        return organMapper.getOrganInfo(organTypeId);
    }
}




