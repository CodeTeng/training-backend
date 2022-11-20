package com.lt.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.common.utils.PageUtils;
import com.lt.common.utils.Query;
import com.lt.constant.UserConstant;
import com.lt.modules.app.entity.dto.UserRegisterRequest;
import com.lt.modules.oss.service.OssService;
import com.lt.modules.sys.mapper.UserMapper;
import com.lt.modules.sys.model.entity.User;
import com.lt.modules.sys.service.RoleService;
import com.lt.modules.sys.service.UserRoleService;
import com.lt.modules.sys.service.UserService;
import com.lt.common.ErrorCode;
import com.lt.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;


import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * 用户服务实现类
 *
 * @author teng
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private OssService ossService;

    @Override
    public User queryByUserName(String username) {
        if (StringUtils.isBlank(username)) {
            return null;
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String username = (String) params.get("username");
        IPage<User> page = this.page(
                new Query<User>().getPage(params),
                new QueryWrapper<User>()
                        .like(StringUtils.isNotBlank(username), "username", username)
                        .eq("isDelete", 0)
        );
        return new PageUtils(page);
    }

    @Override
    public boolean updatePassword(Long userId, String password, String newPassword) {
        User user = new User();
        user.setPassword(newPassword);
        return this.update(user, new QueryWrapper<User>().eq("id", userId).eq("password", password));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveUser(User user) {
        user.setPassword(new Sha256Hash(user.getPassword()).toHex());
        // 添加的用户状态正常
        user.setStatus(0);
        user.setAvatar("https://teng-1310538376.cos.ap-chongqing.myqcloud.com/3718/202211171417939.png");
        this.save(user);
        // 检查角色是否越权
        checkRole(user);
        // 保存用户与角色的关系
        userRoleService.saveOrUpdate(user.getId(), user.getRoleIdList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(User user) {
        if (StringUtils.isBlank(user.getPassword())) {
            user.setPassword(null);
        } else {
            user.setPassword(new Sha256Hash(user.getPassword()).toHex());
        }
        this.updateById(user);
        // 检查角色是否越权
        checkRole(user);
        // 保存用户与角色关系
        userRoleService.saveOrUpdate(user.getId(), user.getRoleIdList());
    }

    @Override
    public void deleteBatch(Long[] userIds) {
        // 逻辑删除用户
        this.removeByIds(Arrays.asList(userIds));
    }

    @Override
    public List<Long> queryAllMenuId(Long userId) {
        return userMapper.queryAllMenuId(userId);
    }

    private void checkRole(User user) {
        // 未分配角色 直接返回
        if (user.getRoleIdList() == null || user.getRoleIdList().size() == 0) {
            return;
        }
        // 查询用户创建的角色列表
        User curUser = (User) SecurityUtils.getSubject().getPrincipal();
        List<Long> roleIdList = roleService.queryRoleIdList(curUser.getId());
        // 判断是否越权
        if (!roleIdList.containsAll(user.getRoleIdList())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "新增用户所选角色，不是本人创建");
        }
    }
}




