package com.lt.modules.sys.service;

import com.lt.modules.sys.model.entity.RoleMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author teng
 * @description 针对表【role_menu(角色菜单关联表)】的数据库操作Service
 * @createDate 2022-11-16 19:41:17
 */
public interface RoleMenuService extends IService<RoleMenu> {

    /**
     * 根据角色ID，获取菜单ID列表
     */
    List<Long> queryMenuIdList(Long roleId);

    void saveOrUpdate(Long roleId, List<Long> menuIdList);

    void deleteBatch(Long[] roleIds);
}
