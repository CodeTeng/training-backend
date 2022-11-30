package com.lt.modules.sys.mapper;

import com.lt.modules.sys.model.entity.MessageUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * @author teng
 * @description 针对表【message_user(学员消息关联表)】的数据库操作Mapper
 * @createDate 2022-11-20 16:35:21
 * @Entity com.lt.modules.sys.model.entity.MessageUser
 */
@Repository
public interface MessageUserMapper extends BaseMapper<MessageUser> {

}




