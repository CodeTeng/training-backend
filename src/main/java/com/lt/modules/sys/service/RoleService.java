package com.lt.modules.sys.service;

import com.lt.common.utils.PageUtils;
import com.lt.modules.sys.model.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @author teng
 * @description 针对表【role(角色表)】的数据库操作Service
 * @createDate 2022-11-16 19:41:17
 */
public interface RoleService extends IService<Role> {
    /**
     * 查询用户创建的角色ID列表
     */
    List<Long> queryRoleIdList(Long userId);

    PageUtils queryPage(Map<String, Object> params);

    void saveRole(Role role);

    void update(Role role);

    void delete(Long roleId);
}
