package com.lt.modules.sys.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lt.common.utils.PageUtils;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lt.modules.sys.model.entity.Message;
import com.lt.modules.sys.model.vo.message.MessageVO;

import java.util.List;
import java.util.Map;

/**
 * @author teng
 * @description 针对表【message(消息表)】的数据库操作Service
 * @createDate 2022-11-20 16:35:21
 */
public interface MessageService extends IService<Message> {

    List<Message> getUserMessage(Long userId);

    void saveMessage(Message message, List<Long> userIds);

    void updateMessage(Message message, List<Long> userIds);

    Page<Message> queryPage(Integer pageNo, Integer pageSize, String title);
}
