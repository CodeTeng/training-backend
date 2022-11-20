package com.lt.modules.sys.service;

import com.lt.common.utils.PageUtils;
import com.lt.modules.sys.model.dto.message.MessageAddRequest;
import com.lt.modules.sys.model.entity.Message;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @author teng
 * @description 针对表【message(消息表)】的数据库操作Service
 * @createDate 2022-11-20 16:35:21
 */
public interface MessageService extends IService<Message> {

    PageUtils queryPage(Map<String, Object> params);

    List<Message> getUserMessage(Long userId);

    void saveMessage(Message message, List<Long> userIds);

    void updateMessage(Message message, List<Long> userIds);
}
