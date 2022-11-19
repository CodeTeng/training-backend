package com.lt.modules.sys.mapper;

import com.lt.modules.sys.model.entity.Notice;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * @author teng
 * @description 针对表【notice(公告表)】的数据库操作Mapper
 * @createDate 2022-11-20 00:06:46
 * @Entity com.lt.modules.sys.model.entity.Notice
 */
@Repository
public interface NoticeMapper extends BaseMapper<Notice> {

}




