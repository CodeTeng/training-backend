package com.lt.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.common.utils.PageUtils;
import com.lt.common.utils.Query;
import com.lt.modules.sys.mapper.UserMapper;
import com.lt.modules.sys.model.entity.Role;
import com.lt.modules.sys.mapper.RoleMapper;
import com.lt.modules.sys.service.RoleMenuService;
import com.lt.modules.sys.service.RoleService;
import com.lt.modules.sys.service.UserRoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private RoleMenuService roleMenuService;

    @Autowired
    private UserRoleService userRoleService;

    @Override
    public List<Long> queryRoleIdList(Long userId) {
        return roleMapper.queryRoleIdList(userId);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String name = (String) params.get("name");
        IPage<Role> page = this.page(
                new Query<Role>().getPage(params),
                new QueryWrapper<Role>()
                        .like(StringUtils.isNotBlank(name), "name", name)
        );
        return new PageUtils(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRole(Role role) {
        this.save(role);
        // 保存角色与菜单关系
        roleMenuService.saveOrUpdate(role.getId(), role.getMenuIdList());
    }

    @Override
    public void update(Role role) {
        this.updateById(role);
        // 更新角色与菜单关系
        roleMenuService.saveOrUpdate(role.getId(), role.getMenuIdList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long roleId) {
        // 删除角色
        roleMapper.deleteById(roleId);
        // 删除角色与菜单关联
        roleMenuService.deleteBatch(new Long[]{roleId});
        // 删除角色与用户关联
        userRoleService.deleteBatch(new Long[]{roleId});
    }
}




