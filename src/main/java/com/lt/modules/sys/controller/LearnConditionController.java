package com.lt.modules.sys.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lt.common.BaseResponse;
import com.lt.common.ErrorCode;
import com.lt.common.utils.ResultUtils;
import com.lt.modules.sys.model.entity.LearnCondition;
import com.lt.modules.sys.model.entity.User;
import com.lt.modules.sys.model.entity.Video;
import com.lt.modules.sys.model.vo.learn.LearnConditionVO;
import com.lt.modules.sys.service.LearnConditionService;
import com.lt.modules.sys.service.UserService;
import com.lt.modules.sys.service.VideoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @description: 学习情况后台管理
 * @author: ~Teng~
 * @date: 2022/12/1 15:53
 */
@RestController
@RequestMapping("/sys/learn")
public class LearnConditionController extends AbstractController {

    @Autowired
    private LearnConditionService learnConditionService;

    @Autowired
    private UserService userService;

    @Autowired
    private VideoService videoService;

    /**
     * 获取所有学员学习情况
     */
    @GetMapping("/queryPage")
    public BaseResponse queryPage(Integer pageNo, Integer pageSize,
                                  @RequestParam(required = false) String username,
                                  @RequestParam(required = false) Integer isDone) {
        if (pageNo == null || pageNo <= 0 || pageSize == null || pageSize <= 0) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "分页参数错误");
        }
        Page<LearnConditionVO> page = learnConditionService.queryPage(pageNo, pageSize, username, isDone);
        return ResultUtils.success(page);
    }

    @GetMapping("/getOneLearn/{id}")
    public BaseResponse getOneLearn(@PathVariable("id") Long id) {
        if (id == null || id <= 0) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "参数错误");
        }
        LearnCondition learnCondition = learnConditionService.getById(id);
        if (learnCondition == null) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "为查询到相关信息");
        }
        LearnConditionVO learnConditionVO = new LearnConditionVO();
        BeanUtils.copyProperties(learnCondition, learnConditionVO);
        User user = userService.getById(learnCondition.getUserId());
        // 设置学员账号
        learnConditionVO.setUsername(user.getUsername());
        // 设置视频标题
        Video video = videoService.getById(learnCondition.getVideoId());
        learnConditionVO.setVideoTitle(video.getVideoTitle());
        return ResultUtils.success(learnConditionVO);
    }
}
