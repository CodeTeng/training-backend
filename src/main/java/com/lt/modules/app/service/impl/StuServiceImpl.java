package com.lt.modules.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lt.common.ErrorCode;
import com.lt.common.exception.BusinessException;
import com.lt.modules.app.entity.dto.UserRegisterRequest;
import com.lt.modules.app.service.StuService;
import com.lt.modules.sys.mapper.UserMapper;
import com.lt.modules.sys.model.entity.User;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description: 前台用户
 * @author: ~Teng~
 * @date: 2022/11/20 20:55
 */
@Service
public class StuServiceImpl implements StuService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public long userRegister(UserRegisterRequest userRegisterRequest) {
        String password = userRegisterRequest.getPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String username = userRegisterRequest.getUsername();
        // 密码和校验密码相同
        if (!password.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次输入的密码不一致");
        }
        synchronized (username.intern()) {
            // 账户不能重复
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("username", username);
            long count = userMapper.selectCount(queryWrapper);
            if (count > 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号重复");
            }
            // 加密
            String encryptPassword = new Sha256Hash(password).toHex();
            // 插入数据
            User user = new User();
            BeanUtils.copyProperties(userRegisterRequest, user);
            user.setPassword(encryptPassword);
            user.setCreator(username);
            user.setAvatar("https://teng-1310538376.cos.ap-chongqing.myqcloud.com/3718/202211171417939.png");
            int res = userMapper.insert(user);
            if (res < 1) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "注册失败，数据库错误");
            }
            return user.getId();
        }
    }
}
