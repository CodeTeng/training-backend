package com.lt.modules.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.modules.sys.model.entity.MessageUser;
import com.lt.modules.sys.service.MessageUserService;
import com.lt.modules.sys.mapper.MessageUserMapper;
import org.springframework.stereotype.Service;

/**
* @author teng
* @description 针对表【message_user(学员消息关联表)】的数据库操作Service实现
* @createDate 2022-11-20 16:35:21
*/
@Service
public class MessageUserServiceImpl extends ServiceImpl<MessageUserMapper, MessageUser>
    implements MessageUserService{

}




