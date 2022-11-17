package com.lt.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.modules.sys.model.entity.UserRole;
import com.lt.modules.sys.mapper.UserRoleMapper;
import com.lt.modules.sys.service.UserRoleService;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author teng
 * @description 针对表【user_role(用户角色关联表)】的数据库操作Service实现
 * @createDate 2022-11-16 19:41:17
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole>
        implements UserRoleService {

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public List<Long> queryRoleIdList(Long userId) {
        return userRoleMapper.queryRoleIdList(userId);
    }

    @Override
    public void saveOrUpdate(Long userId, List<Long> roleIdList) {
        // 先删除用户与角色关系
        QueryWrapper<UserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", userId);
        this.remove(queryWrapper);
        if (roleIdList == null || roleIdList.size() == 0) {
            return;
        }
        // 保存用户与角色关系
        for (Long roleId : roleIdList) {
            UserRole userRole = new UserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(roleId);
            this.save(userRole);
        }
    }
}




