package com.lt.modules.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.modules.sys.model.entity.Role;
import com.lt.modules.sys.mapper.RoleMapper;
import com.lt.modules.sys.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author teng
 * @description 针对表【role(角色表)】的数据库操作Service实现
 * @createDate 2022-11-16 19:41:17
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role>
        implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public List<Long> queryRoleIdList(Long userId) {
        return roleMapper.queryRoleIdList(userId);
    }
}




