package com.lt.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.modules.sys.model.entity.LearnCondition;
import com.lt.modules.sys.model.entity.User;
import com.lt.modules.sys.model.entity.Video;
import com.lt.modules.sys.model.vo.learn.LearnConditionVO;
import com.lt.modules.sys.service.LearnConditionService;
import com.lt.modules.sys.mapper.LearnConditionMapper;
import com.lt.modules.sys.service.UserService;
import com.lt.modules.sys.service.VideoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping;

import java.util.List;

/**
 * @author teng
 * @description 针对表【learn_condition(学习情况表)】的数据库操作Service实现
 * @createDate 2022-11-30 14:28:26
 */
@Service
public class LearnConditionServiceImpl extends ServiceImpl<LearnConditionMapper, LearnCondition>
        implements LearnConditionService {

    @Autowired
    private UserService userService;

    @Autowired
    private VideoService videoService;

    @Override
    public Page<LearnConditionVO> queryPage(Integer pageNo, Integer pageSize, String username, Integer isDone) {
        QueryWrapper<LearnCondition> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(username), "creator", username);
        queryWrapper.eq(isDone != null, "isDone", isDone);
        Page<LearnCondition> learnConditionPage = this.page(
                new Page<>(pageNo, pageSize),
                queryWrapper
        );
        List<LearnCondition> learnConditionList = learnConditionPage.getRecords();
        List<LearnConditionVO> learnConditionVOList = learnConditionList.stream().map(learnCondition -> {
            LearnConditionVO learnConditionVO = new LearnConditionVO();
            BeanUtils.copyProperties(learnCondition, learnConditionVO);
            User user = userService.getById(learnCondition.getUserId());
            // 设置学员账号
            learnConditionVO.setUsername(user.getUsername());
            // 设置视频标题
            Video video = videoService.getById(learnCondition.getVideoId());
            learnConditionVO.setVideoTitle(video.getVideoTitle());
            return learnConditionVO;
        }).toList();
        Page<LearnConditionVO> page = new Page<>();
        BeanUtils.copyProperties(learnConditionPage, page);
        page.setRecords(learnConditionVOList);
        return page;
    }
}




