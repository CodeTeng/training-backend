package com.lt.modules.sys.mapper;

import com.lt.modules.sys.model.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author teng
 * @description 针对表【role(角色表)】的数据库操作Mapper
 * @createDate 2022-11-16 19:41:17
 * @Entity com.lt.model.entity.Role
 */
@Repository
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 查询用户创建的角色ID列表
     */
    List<Long> queryRoleIdList(Long userId);
}




