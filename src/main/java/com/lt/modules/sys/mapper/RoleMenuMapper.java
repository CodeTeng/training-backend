package com.lt.modules.sys.mapper;

import com.lt.modules.sys.model.entity.RoleMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author teng
 * @description 针对表【role_menu(角色菜单关联表)】的数据库操作Mapper
 * @createDate 2022-11-16 19:41:17
 * @Entity com.lt.model.entity.RoleMenu
 */
@Repository
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {

    /**
     * 根据角色ID，获取菜单ID列表
     */
    List<Long> queryMenuIdList(Long roleId);

    /**
     * 根据角色ID数组，批量删除
     */
    void deleteBatch(Long[] roleIds);
}




