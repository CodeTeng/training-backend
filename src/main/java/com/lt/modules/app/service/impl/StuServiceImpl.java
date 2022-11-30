package com.lt.modules.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lt.common.ErrorCode;
import com.lt.common.exception.BusinessException;
import com.lt.modules.app.model.dto.StuRegisterRequest;
import com.lt.modules.app.model.dto.StuUpdateRequest;
import com.lt.modules.app.model.vo.StuVideoVO;
import com.lt.modules.app.service.StuService;
import com.lt.modules.sys.mapper.UserMapper;
import com.lt.modules.sys.model.entity.User;
import com.lt.modules.sys.model.entity.Video;
import com.lt.modules.sys.service.ExamRecordService;
import com.lt.modules.sys.service.VideoService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @description: 前台用户
 * @author: ~Teng~
 * @date: 2022/11/20 20:55
 */
@Service
public class StuServiceImpl implements StuService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private VideoService videoService;

    @Override
    public long userRegister(StuRegisterRequest stuRegisterRequest) {
        String password = stuRegisterRequest.getPassword();
        String checkPassword = stuRegisterRequest.getCheckPassword();
        String username = stuRegisterRequest.getUsername();
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
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号重复，请重新输入");
            }
            // 加密
            String encryptPassword = new Sha256Hash(password).toHex();
            // 插入数据
            User user = new User();
            BeanUtils.copyProperties(stuRegisterRequest, user);
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

    @Override
    public boolean updateMyInfo(StuUpdateRequest stuUpdateRequest) {
        User user = new User();
        BeanUtils.copyProperties(stuUpdateRequest, user);
        int flag = userMapper.updateById(user);
        return flag > 0;
    }

    @Override
    public Page<StuVideoVO> getMyPlanVideo(Integer pageNo, Integer pageSize, Long organPlanId, Long videoTypeId, String videoTitle) {
        QueryWrapper<Video> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(organPlanId != null && organPlanId > 0, "organPlanId", organPlanId);
        queryWrapper.eq(videoTypeId != null && videoTypeId > 0, "videoTypeId", videoTypeId);
        queryWrapper.like(StringUtils.isNotBlank(videoTitle), "videoTitle", videoTitle);
        queryWrapper.eq("status", 0);
        queryWrapper.eq("isPublish", 0);
        Page<Video> page = videoService.page(
                new Page<>(pageNo, pageSize),
                queryWrapper
        );
        Page<StuVideoVO> resPage = new Page<>();
        BeanUtils.copyProperties(page, resPage);
        List<StuVideoVO> stuVideoVOList = page.getRecords().stream().map(video -> {
            StuVideoVO stuVideoVO = new StuVideoVO();
            BeanUtils.copyProperties(video, stuVideoVO);
            return stuVideoVO;
        }).toList();
        resPage.setRecords(stuVideoVOList);
        return resPage;
    }
}
