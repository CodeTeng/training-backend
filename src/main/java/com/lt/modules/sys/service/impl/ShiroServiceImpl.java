package com.lt.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lt.constant.UserConstant;
import com.lt.modules.sys.mapper.MenuMapper;
import com.lt.modules.sys.model.entity.Menu;
import com.lt.modules.sys.model.entity.User;
import com.lt.modules.sys.model.entity.UserToken;
import com.lt.modules.sys.mapper.UserMapper;
import com.lt.modules.sys.mapper.UserTokenMapper;
import com.lt.modules.sys.service.ShiroService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2022/11/17 11:34
 */
@Service
public class ShiroServiceImpl implements ShiroService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private UserTokenMapper userTokenMapper;

    @Override
    public Set<String> getUserPermissions(long userId) {
        List<String> permsList;
        User user = userMapper.selectById(userId);
        String username = user.getUsername();
        // 系统管理员，拥有最高权限
        if (UserConstant.SUPER_ADMIN_ROLE.equals(username)) {
            List<Menu> menuList = menuMapper.selectList(null);
            permsList = new ArrayList<>(menuList.size());
            for (Menu menu : menuList) {
                permsList.add(menu.getPermission());
            }
        } else {
            permsList = userMapper.queryAllPerms(userId);
        }
        // 用户权限列表
        Set<String> permsSet = new HashSet<>();
        for (String perms : permsList) {
            if (StringUtils.isBlank(perms)) {
                continue;
            }
            permsSet.addAll(Arrays.asList(perms.trim().split(",")));
        }
        return permsSet;
    }

    @Override
    public UserToken queryByToken(String token) {
        QueryWrapper<UserToken> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("token", token);
        return userTokenMapper.selectOne(queryWrapper);
    }

    @Override
    public User queryUser(Long userId) {
        return userMapper.selectById(userId);
    }
}
