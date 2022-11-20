package com.lt.modules.sys.service;

import com.lt.common.utils.PageUtils;
import com.lt.modules.sys.model.entity.Notice;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * @author teng
 * @description 针对表【notice(公告表)】的数据库操作Service
 * @createDate 2022-11-20 00:06:46
 */
public interface NoticeService extends IService<Notice> {

    PageUtils queryPage(Map<String, Object> map);
}
