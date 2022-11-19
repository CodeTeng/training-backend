package com.lt.modules.sys.mapper;

import com.lt.modules.sys.model.entity.Organ;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lt.modules.sys.model.vo.OrganVO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author teng
 * @description 针对表【organ(培训机构表)】的数据库操作Mapper
 * @createDate 2022-11-19 14:50:36
 * @Entity com.lt.modules.sys.model.entity.Organ
 */
@Repository
public interface OrganMapper extends BaseMapper<Organ> {

    /**
     * 根据机构类型查找机构全部信息
     */
    List<OrganVO> getOrganInfo(Long organTypeId);
}




