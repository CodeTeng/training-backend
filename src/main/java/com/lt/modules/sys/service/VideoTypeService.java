package com.lt.modules.sys.service;

import com.lt.common.utils.PageUtils;
import com.lt.modules.sys.model.entity.VideoType;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * @author teng
 * @description 针对表【video_type(视频类别表)】的数据库操作Service
 * @createDate 2022-11-21 15:23:47
 */
public interface VideoTypeService extends IService<VideoType> {

    PageUtils queryPage(Map<String, Object> params);
}
