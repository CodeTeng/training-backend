package com.lt.modules.sys.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lt.common.BaseResponse;
import com.lt.common.ErrorCode;
import com.lt.common.annotation.SysLog;
import com.lt.common.utils.ResultUtils;
import com.lt.modules.sys.model.entity.Organ;
import com.lt.modules.sys.model.entity.OrganPlan;
import com.lt.modules.sys.model.entity.Video;
import com.lt.modules.sys.model.entity.VideoType;
import com.lt.modules.sys.model.vo.video.VideoVO;
import com.lt.modules.sys.service.OrganPlanService;
import com.lt.modules.sys.service.OrganService;
import com.lt.modules.sys.service.VideoService;
import com.lt.modules.sys.service.VideoTypeService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @description: 视频管理
 * @author: ~Teng~
 * @date: 2022/11/21 17:00
 */
@RestController
@RequestMapping("/sys/video")
public class VideoController extends AbstractController {

    @Autowired
    private VideoService videoService;

    @Autowired
    private VideoTypeService videoTypeService;

    @Autowired
    private OrganPlanService organPlanService;

    @Autowired
    private OrganService organService;

    /**
     * 分页获取所有视频
     */
    @GetMapping("/queryPage")
    @RequiresPermissions("sys:video:list")
    public BaseResponse queryPage(Integer pageNo, Integer pageSize,
                                  @RequestParam(required = false) String videoTitle,
                                  @RequestParam(required = false) Long organId,
                                  @RequestParam(required = false) Long videoTypeId) {
        if (pageNo == null || pageNo <= 0 || pageSize == null || pageSize <= 0) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "分页参数错误");
        }
        Page<VideoVO> page = videoService.queryPage(pageNo, pageSize, organId, videoTypeId, videoTitle);
        return ResultUtils.success(page);
    }

    /**
     * 获取单个视频
     */
    @GetMapping("/getVideoById/{videoId}")
    @RequiresPermissions("sys:video:list")
    public BaseResponse getVideoById(@PathVariable("videoId") Long videoId) {
        if (videoId == null || videoId <= 0) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "视频参数错误");
        }
        Video video = videoService.getById(videoId);
        if (video == null) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "未查询到相关视频");
        }
        VideoVO videoVO = new VideoVO();
        BeanUtils.copyProperties(video, videoVO);
        // 设置类型名称
        VideoType videoType = videoTypeService.getById(video.getVideoTypeId());
        videoVO.setTypeName(videoType.getTypeName());
        // 设置机构名称
        OrganPlan organPlan = organPlanService.getById(video.getOrganPlanId());
        Organ organ = organService.getById(organPlan.getOrganId());
        videoVO.setOrganName(organ.getName());
        return ResultUtils.success(videoVO);
    }

    @PostMapping("/updateVideo")
    @SysLog("更新视频")
    @RequiresPermissions("sys:video:update")
    public BaseResponse updateVideo(@RequestBody Video video) {
        if (video == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "视频参数错误");
        }
        String videoTitle = video.getVideoTitle();
        String videoUrl = video.getVideoUrl();
        String coverUrl = video.getCoverUrl();
        if (StringUtils.isAnyBlank(videoTitle, videoUrl, coverUrl)) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "请填写完整相关视频信息");
        }
        Long organPlanId = video.getOrganPlanId();
        if (organPlanId == null || organPlanId <= 0) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "视频所属培训计划错误");
        }
        Integer status = video.getStatus();
        if (status == null || (status != 0 && status != 1 && status != 2)) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "视频状态参数错误");
        }
        Integer isPublish = video.getIsPublish();
        if (isPublish == null || (isPublish != 0 && isPublish != 1)) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "视频发布参数错误");
        }
        video.setUpdater(getUser().getUsername());
        if (status == 1 && isPublish == 0) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "视频停用时无法发布");
        }
        if (status == 2 && isPublish == 0) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "视频处于审核状态无法发布");
        }
        boolean flag = videoService.updateById(video);
        if (!flag) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "更新失败");
        }
        return ResultUtils.success("更新成功");
    }

    @PostMapping("/saveVideo")
    @SysLog("保存视频")
    @RequiresPermissions("sys:video:save")
    public BaseResponse saveVideo(@RequestBody Video video) {
        if (video == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "视频参数错误");
        }
        String videoTitle = video.getVideoTitle();
        String videoUrl = video.getVideoUrl();
        String coverUrl = video.getCoverUrl();
        if (StringUtils.isAnyBlank(videoTitle, videoUrl, coverUrl)) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "请填写完整相关视频信息");
        }
        Long organPlanId = video.getOrganPlanId();
        if (organPlanId == null || organPlanId <= 0) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "视频所属培训计划错误");
        }
        // 设置默认状态
        video.setStatus(2);
        video.setIsPublish(1);
        video.setCreator(getUser().getUsername());
        boolean flag = videoService.save(video);
        if (!flag) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "添加失败");
        }
        return ResultUtils.success("添加成功");
    }

    @PostMapping("/deleteVideo/{videoId}")
    @SysLog("删除视频")
    @RequiresPermissions("sys:video:delete")
    public BaseResponse deleteVideo(@PathVariable("videoId") Long videoId) {
        if (videoId == null || videoId <= 0) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "视频参数错误");
        }
        Video video = videoService.getById(videoId);
        if (video == null) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "未查询到相关视频");
        }
        video.setUpdater(getUser().getUsername());
        boolean flag = videoService.removeById(video);
        if (!flag) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "删除失败");
        }
        return ResultUtils.success("删除成功");
    }
}
