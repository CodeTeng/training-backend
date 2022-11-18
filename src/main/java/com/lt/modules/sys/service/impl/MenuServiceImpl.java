package com.lt.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.modules.sys.model.entity.Menu;
import com.lt.modules.sys.model.entity.RoleMenu;
import com.lt.modules.sys.service.MenuService;
import com.lt.modules.sys.mapper.MenuMapper;
import com.lt.modules.sys.service.RoleMenuService;
import com.lt.modules.sys.service.UserService;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author teng
 * @description 针对表【menu(菜单表)】的数据库操作Service实现
 * @createDate 2022-11-17 17:08:18
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu>
        implements MenuService {

    @Autowired
    private UserService userService;

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private RoleMenuService roleMenuService;

    @Override
    public List<Menu> getUserMenuList(Long userId) {
        if (userId == 1L) {
            // 超级管理员 最高权限
            return getMenuList(null);
        }
        // 用户菜单列表
        List<Long> menuIdList = userService.queryAllMenuId(userId);
        return getMenuList(menuIdList);
    }

    @Override
    public List<Menu> queryNotButtonList() {
        return menuMapper.queryNotButtonList();
    }

    @Override
    public List<Menu> queryListParentId(Long parentId) {
        return menuMapper.queryListParentId(parentId);
    }

    @Override
    public void delete(Long menuId) {
        // 删除菜单
        this.removeById(menuId);
        // 删除菜单与角色关联
        QueryWrapper<RoleMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("menuId", menuId);
        roleMenuService.remove(queryWrapper);
    }

    /**
     * 获取拥有的菜单列表
     */
    private List<Menu> getMenuList(List<Long> menuIdList) {
        // 查询拥有的所有菜单
        List<Menu> menus = this.baseMapper.selectList(new QueryWrapper<Menu>()
                .in(Objects.nonNull(menuIdList), "id", menuIdList).in("type", 0, 1));
        // 查询完成 对此list直接排序
        Collections.sort(menus);
        // 将id和菜单绑定
        HashMap<Long, Menu> menuMap = new HashMap<>(12);
        for (Menu menu : menus) {
            menuMap.put(menu.getId(), menu);
        }
        // 使用迭代器,组装菜单的层级关系
        Iterator<Menu> iterator = menus.iterator();
        while (iterator.hasNext()) {
            Menu menu = iterator.next();
            Menu parent = menuMap.get(menu.getParentId());
            if (Objects.nonNull(parent)) {
                parent.getList().add(menu);
                // 将这个菜单从当前节点移除
                iterator.remove();
            }
        }
        return menus;
    }
}




