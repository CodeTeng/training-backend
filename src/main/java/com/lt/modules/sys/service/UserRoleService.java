package com.lt.modules.sys.service;

import com.lt.modules.sys.model.entity.UserRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author teng
 * @description 针对表【user_role(用户角色关联表)】的数据库操作Service
 * @createDate 2022-11-16 19:41:17
 */
public interface UserRoleService extends IService<UserRole> {

    /**
     * 根据用户ID，获取角色ID列表
     */
    List<Long> queryRoleIdList(Long userId);


    void saveOrUpdate(Long userId, List<Long> roleIdList);
}
