package com.lt.modules.sys.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lt.modules.sys.model.entity.Video;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lt.modules.sys.model.vo.video.VideoVO;

/**
 * @author teng
 * @description 针对表【video(视频表)】的数据库操作Service
 * @createDate 2022-11-21 15:23:47
 */
public interface VideoService extends IService<Video> {

    Page<VideoVO> queryPage(Integer pageNo, Integer pageSize, Long organId, Long videoTypeId, String videoTitle);
}
