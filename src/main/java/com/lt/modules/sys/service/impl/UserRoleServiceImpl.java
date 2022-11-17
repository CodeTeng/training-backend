package com.lt.modules.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.modules.sys.model.entity.UserRole;
import com.lt.modules.sys.mapper.UserRoleMapper;
import com.lt.modules.sys.service.UserRoleService;
import org.springframework.stereotype.Service;

/**
* @author teng
* @description 针对表【user_role(用户角色关联表)】的数据库操作Service实现
* @createDate 2022-11-16 19:41:17
*/
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole>
    implements UserRoleService {

}




