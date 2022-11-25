package com.lt.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.common.utils.PageUtils;
import com.lt.common.utils.Query;
import com.lt.modules.sys.model.entity.VideoType;
import com.lt.modules.sys.service.VideoTypeService;
import com.lt.modules.sys.mapper.VideoTypeMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author teng
 * @description 针对表【video_type(视频类别表)】的数据库操作Service实现
 * @createDate 2022-11-21 15:23:47
 */
@Service
public class VideoTypeServiceImpl extends ServiceImpl<VideoTypeMapper, VideoType>
        implements VideoTypeService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String typeName = (String) params.get("typeName");
        IPage<VideoType> page = this.page(
                new Query<VideoType>().getPage(params),
                new QueryWrapper<VideoType>()
                        .like(StringUtils.isNotBlank(typeName), "typeName", typeName)
        );
        return new PageUtils(page);
    }
}




