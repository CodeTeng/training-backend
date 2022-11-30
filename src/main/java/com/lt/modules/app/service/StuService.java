package com.lt.modules.app.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lt.modules.app.model.dto.StuRegisterRequest;
import com.lt.modules.app.model.dto.StuUpdateRequest;
import com.lt.modules.app.model.vo.StuVideoVO;

import javax.servlet.http.HttpServletResponse;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2022/11/20 20:54
 */
public interface StuService {
    /**
     * 用户注册
     */
    long userRegister(StuRegisterRequest stuRegisterRequest);

    /**
     * 更新前台用户信息
     */
    boolean updateMyInfo(StuUpdateRequest stuUpdateRequest);

    /**
     * 获取个人某个培训计划下的视频
     */
    Page<StuVideoVO> getMyPlanVideo(Integer pageNo, Integer pageSize, Long organPlanId, Long videoTypeId, String videoTitle);
}
