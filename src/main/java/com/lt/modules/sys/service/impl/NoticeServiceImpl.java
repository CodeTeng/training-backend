package com.lt.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.common.utils.PageUtils;
import com.lt.common.utils.Query;
import com.lt.modules.sys.model.entity.Notice;
import com.lt.modules.sys.service.NoticeService;
import com.lt.modules.sys.mapper.NoticeMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author teng
 * @description 针对表【notice(公告表)】的数据库操作Service实现
 * @createDate 2022-11-20 00:06:46
 */
@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice>
        implements NoticeService {

    @Override
    public PageUtils queryPage(Map<String, Object> map) {
        String title = (String) map.get("title");
        IPage<Notice> page = this.page(
                new Query<Notice>().getPage(map),
                new QueryWrapper<Notice>()
                        .like(StringUtils.isNotBlank(title), "title", title)
                        .eq("isDelete", 0)
        );
        return new PageUtils(page);
    }
}




