package com.lt.modules.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.modules.sys.model.entity.Menu;
import com.lt.modules.sys.mapper.MenuMapper;
import com.lt.modules.sys.service.MenuService;
import org.springframework.stereotype.Service;

/**
* @author teng
* @description 针对表【menu(菜单表)】的数据库操作Service实现
* @createDate 2022-11-16 19:41:17
*/
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu>
    implements MenuService {

}




