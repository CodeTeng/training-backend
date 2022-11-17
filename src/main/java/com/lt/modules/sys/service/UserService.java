package com.lt.modules.sys.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.lt.modules.sys.model.entity.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户服务
 *
 * @author yupi
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @param username      用户账户
     * @param password      用户密码
     * @param nickName      用户昵称
     * @param checkPassword 校验密码
     * @return 新用户 id
     */
    long userRegister(String username, String password, String nickName, String checkPassword);

    /**
     * 根据用户名查询对应角色信息
     *
     * @param principal 用户身份---用户名
     * @return 用户角色信息
     */
    List<String> getUserRoleInfo(String principal);

    /**
     * 根据角色名查询对应权限信息
     *
     * @param roles 角色名集合
     * @return 该角色对应的权限集合
     */
    List<String> getUserPermissionInfo(List<String> roles);

    /**
     * 根据用户名获取用户
     *
     * @param username 用户名
     * @return 用户
     */
    User queryByUserName(String username);
}
