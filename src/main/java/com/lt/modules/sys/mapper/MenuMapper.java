package com.lt.modules.sys.mapper;

import com.lt.modules.sys.model.entity.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author teng
 * @description 针对表【menu(菜单表)】的数据库操作Mapper
 * @createDate 2022-11-17 17:08:18
 * @Entity com.lt.modules.sys.model.entity.Menu
 */
@Repository
public interface MenuMapper extends BaseMapper<Menu> {

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
}




