package com.lt.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.modules.sys.model.entity.Organ;
import com.lt.modules.sys.model.entity.OrganPlan;
import com.lt.modules.sys.model.entity.Video;
import com.lt.modules.sys.model.entity.VideoType;
import com.lt.modules.sys.model.vo.video.VideoVO;
import com.lt.modules.sys.service.OrganPlanService;
import com.lt.modules.sys.service.OrganService;
import com.lt.modules.sys.service.VideoService;
import com.lt.modules.sys.mapper.VideoMapper;
import com.lt.modules.sys.service.VideoTypeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author teng
 * @description 针对表【video(视频表)】的数据库操作Service实现
 * @createDate 2022-11-21 15:23:47
 */
@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video>
        implements VideoService {

    @Autowired
    private OrganPlanService organPlanService;

    @Autowired
    private VideoTypeService videoTypeService;

    @Autowired
    private OrganService organService;

    @Override
    public Page<VideoVO> queryPage(Integer pageNo, Integer pageSize, Long organId, Long videoTypeId, String videoTitle) {
        QueryWrapper<Video> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(videoTypeId != null && videoTypeId > 0, "videoTypeId", videoTypeId);
        queryWrapper.like(StringUtils.isNotBlank(videoTitle), "videoTitle", videoTitle);
        if (organId != null && organId > 0) {
            // 查看该机构下的所有视频
            List<OrganPlan> organPlanList = organPlanService.list(new QueryWrapper<OrganPlan>().eq("organId", organId));
            if (organPlanList != null && organPlanList.size() > 0) {
                List<Long> organPlanIdList = organPlanList.stream().map(OrganPlan::getId).toList();
                queryWrapper.in("organPlanId", organPlanIdList);
            }
        }
        Page<Video> videoPage = this.page(
                new Page<>(pageNo, pageSize),
                queryWrapper
        );
        List<Video> videoList = videoPage.getRecords();
        List<VideoVO> videoVOList = videoList.stream().map(video -> {
            VideoVO videoVO = new VideoVO();
            BeanUtils.copyProperties(video, videoVO);
            // 设置类型名称
            VideoType videoType = videoTypeService.getById(video.getVideoTypeId());
            videoVO.setTypeName(videoType.getTypeName());
            // 设置机构名称
            OrganPlan organPlan = organPlanService.getById(video.getOrganPlanId());
            Organ organ = organService.getById(organPlan.getOrganId());
            videoVO.setOrganName(organ.getName());
            return videoVO;
        }).toList();
        Page<VideoVO> page = new Page<>();
        BeanUtils.copyProperties(videoPage, page);
        page.setRecords(videoVOList);
        return page;
    }
}




