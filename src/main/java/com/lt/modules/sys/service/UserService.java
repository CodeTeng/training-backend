package com.lt.modules.sys.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lt.common.utils.PageUtils;
import com.lt.modules.sys.model.dto.user.UserAddRequest;
import com.lt.modules.sys.model.dto.user.UserUpdateRequest;
import com.lt.modules.sys.model.entity.User;
import com.lt.modules.sys.model.vo.UserInfoVO;

import java.util.List;
import java.util.Map;

/**
 * 用户服务
 *
 * @author teng
 */
public interface UserService extends IService<User> {

    /**
     * 根据用户名获取用户
     *
     * @param username 用户名
     * @return 用户
     */
    User queryByUserName(String username);

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
    void saveUser(UserAddRequest userAddRequest);

    /**
     * 修改用户
     */
    void update(UserUpdateRequest userUpdateRequest);

    /**
     * 删除用户---反序列化错误
     */
    void deleteBatch(Long[] userIds);

    /**
     * 查询用户的所有菜单ID
     */
    List<Long> queryAllMenuId(Long userId);

    Page<UserInfoVO> queryPage(Integer pageNo, Integer pageSize, String username, String organName);
}
