package com.lt.modules.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.modules.sys.model.entity.Notice;
import com.lt.modules.sys.service.NoticeService;
import com.lt.modules.sys.mapper.NoticeMapper;
import org.springframework.stereotype.Service;

/**
* @author teng
* @description 针对表【notice(公告表)】的数据库操作Service实现
* @createDate 2022-11-20 00:06:46
*/
@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice>
    implements NoticeService{

}




