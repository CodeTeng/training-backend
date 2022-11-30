package com.lt.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.modules.sys.mapper.UserMapper;
import com.lt.modules.sys.model.dto.user.UserAddRequest;
import com.lt.modules.sys.model.dto.user.UserUpdateRequest;
import com.lt.modules.sys.model.entity.Organ;
import com.lt.modules.sys.model.entity.Role;
import com.lt.modules.sys.model.entity.User;
import com.lt.modules.sys.model.vo.UserInfoVO;
import com.lt.modules.sys.service.OrganService;
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


import java.util.Arrays;
import java.util.List;


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
    private OrganService organService;

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
    public Page<UserInfoVO> queryPage(Integer pageNo, Integer pageSize, String username, String organName) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(username), "username", username);
        Long organId = null;
        if (StringUtils.isNotBlank(organName)) {
            Organ organ = organService.getOne(new QueryWrapper<Organ>().eq("name", organName));
            organId = organ.getId();
        }
        queryWrapper.eq(organId != null, "organId", organId);
        Page<User> page = this.page(
                new Page<>(pageNo, pageSize),
                queryWrapper
        );
        List<User> records = page.getRecords();
        List<UserInfoVO> userInfoVOList = records.stream().map(user -> {
            UserInfoVO userInfoVO = new UserInfoVO();
            BeanUtils.copyProperties(user, userInfoVO);
            Organ resOrgan = organService.getById(user.getOrganId());
            // 一些管理员哪个机构也不属于
            if (resOrgan == null) {
                userInfoVO.setOrganName(null);
            } else {
                userInfoVO.setOrganName(resOrgan.getName());
            }
            List<String> roleNameList = userRoleService.getUserRoleName(user.getId());
            String roleName = StringUtils.join(roleNameList, ",");
            userInfoVO.setRoleName(roleName);
            return userInfoVO;
        }).toList();
        Page<UserInfoVO> resPage = new Page<>();
        BeanUtils.copyProperties(page, resPage);
        resPage.setRecords(userInfoVOList);
        return resPage;
    }

    @Override
    public boolean updatePassword(Long userId, String password, String newPassword) {
        User user = new User();
        user.setPassword(newPassword);
        return this.update(user, new QueryWrapper<User>().eq("id", userId).eq("password", password));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveUser(UserAddRequest userAddRequest) {
        User user = new User();
        BeanUtils.copyProperties(userAddRequest, user);
        String sexName = userAddRequest.getSexName();
        Integer sex = null;
        if ("男".equals(sexName)) {
            sex = 0;
        } else if ("女".equals(sexName)) {
            sex = 1;
        }
        user.setSex(sex);
        // 添加的用户状态正常
        user.setStatus(0);
        user.setAvatar("https://teng-1310538376.cos.ap-chongqing.myqcloud.com/3718/202211171417939.png");
        user.setPassword(new Sha256Hash(userAddRequest.getPassword()).toHex());
        String organName = userAddRequest.getOrganName();
        Organ organ = organService.getOne(new QueryWrapper<Organ>().eq(StringUtils.isNotBlank(organName), "name", organName));
        if (organ == null) {
            user.setOrganId(null);
        } else {
            user.setOrganId(organ.getId());
        }
        String roleName = userAddRequest.getRoleName();
        List<String> roleNameList = Arrays.stream(StringUtils.split(roleName, ",")).toList();
        List<Long> roleIdList = roleNameList.stream().map(name -> {
            Role role = roleService.getOne(new QueryWrapper<Role>().eq("name", name));
            return role.getId();
        }).toList();
        user.setRoleIdList(roleIdList);
        this.save(user);
        // 检查角色是否越权
        checkRole(user);
        // 保存用户与角色的关系
        userRoleService.saveOrUpdate(user.getId(), user.getRoleIdList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(UserUpdateRequest userUpdateRequest) {
        User user = new User();
        BeanUtils.copyProperties(userUpdateRequest, user);
        Organ organ = organService.getById(userUpdateRequest.getOrganId());
        if (organ == null) {
            user.setOrganId(null);
        } else {
            user.setOrganId(organ.getId());
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




