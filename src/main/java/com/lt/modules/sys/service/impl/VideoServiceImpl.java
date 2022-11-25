package com.lt.modules.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.modules.sys.model.entity.Video;
import com.lt.modules.sys.service.VideoService;
import com.lt.modules.sys.mapper.VideoMapper;
import org.springframework.stereotype.Service;

/**
* @author teng
* @description 针对表【video(视频表)】的数据库操作Service实现
* @createDate 2022-11-21 15:23:47
*/
@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video>
    implements VideoService{

}




