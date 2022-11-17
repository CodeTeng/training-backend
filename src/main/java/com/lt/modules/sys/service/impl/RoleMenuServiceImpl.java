package com.lt.modules.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.modules.sys.model.entity.RoleMenu;
import com.lt.modules.sys.mapper.RoleMenuMapper;
import com.lt.modules.sys.service.RoleMenuService;
import org.springframework.stereotype.Service;

/**
* @author teng
* @description 针对表【role_menu(角色菜单关联表)】的数据库操作Service实现
* @createDate 2022-11-16 19:41:17
*/
@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu>
    implements RoleMenuService {

}




