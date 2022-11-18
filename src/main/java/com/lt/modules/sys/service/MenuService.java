package com.lt.modules.sys.service;

import com.lt.modules.sys.model.entity.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author teng
 * @description 针对表【menu(菜单表)】的数据库操作Service
 * @createDate 2022-11-17 17:08:18
 */
public interface MenuService extends IService<Menu> {

    /**
     * 获取用户菜单列表
     */
    List<Menu> getUserMenuList(Long userId);

    /**
     * 获取不包含按钮的菜单列表
     */
    List<Menu> queryNotButtonList();

    /**
     * 根据父菜单，查询子菜单
     *
     * @param parentId 父菜单ID
     */
    List<Menu> queryListParentId(Long parentId);

    /**
     * 删除
     */
    void delete(Long menuId);
}
