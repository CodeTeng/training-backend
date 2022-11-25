package com.lt.modules.sys.mapper;

import com.lt.modules.sys.model.entity.UserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author teng
 * @description 针对表【user_role(用户角色关联表)】的数据库操作Mapper
 * @createDate 2022-11-16 19:41:17
 * @Entity com.lt.model.entity.UserRole
 */
@Repository
public interface UserRoleMapper extends BaseMapper<UserRole> {

    /**
     * 根据用户ID，获取角色ID列表
     */
    List<Long> queryRoleIdList(Long userId);

    /**
     * 根据角色ID数组，批量删除
     */
    void deleteBatch(Long[] roleIds);

    List<String> getUserRoleName(Long userId);
}




