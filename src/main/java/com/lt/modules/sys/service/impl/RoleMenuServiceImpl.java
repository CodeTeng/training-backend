package com.lt.modules.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.modules.sys.model.entity.RoleMenu;
import com.lt.modules.sys.mapper.RoleMenuMapper;
import com.lt.modules.sys.service.RoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author teng
 * @description 针对表【role_menu(角色菜单关联表)】的数据库操作Service实现
 * @createDate 2022-11-16 19:41:17
 */
@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu>
        implements RoleMenuService {

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Override
    public List<Long> queryMenuIdList(Long roleId) {
        return roleMenuMapper.queryMenuIdList(roleId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(Long roleId, List<Long> menuIdList) {
        // 先删除角色与菜单关系
        deleteBatch(new Long[]{roleId});
        if (menuIdList.size() == 0) {
            return;
        }
        //保存角色与菜单关系
        for (Long menuId : menuIdList) {
            RoleMenu sysRoleMenuEntity = new RoleMenu();
            sysRoleMenuEntity.setMenuId(menuId);
            sysRoleMenuEntity.setRoleId(roleId);
            this.save(sysRoleMenuEntity);
        }
    }

    @Override
    public void deleteBatch(Long[] roleIds) {
        roleMenuMapper.deleteBatch(roleIds);
    }
}




