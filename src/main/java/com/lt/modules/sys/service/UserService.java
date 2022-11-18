package com.lt.modules.sys.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.lt.common.utils.PageUtils;
import com.lt.modules.sys.model.entity.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

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
     * 根据用户名获取用户
     *
     * @param username 用户名
     * @return 用户
     */
    User queryByUserName(String username);

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 修改密码
     *
     * @param userId      用户ID
     * @param password    原密码
     * @param newPassword 新密码
     */
    boolean updatePassword(Long userId, String password, String newPassword);

    /**
     * 添加用户
     */
    void saveUser(User user);

    /**
     * 修改用户
     */
    void update(User user);

    /**
     * 删除用户---反序列化错误
     */
    void deleteBatch(Long[] userIds);

    /**
     * 查询用户的所有菜单ID
     */
    List<Long> queryAllMenuId(Long userId);
}
