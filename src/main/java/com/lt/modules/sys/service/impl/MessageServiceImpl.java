package com.lt.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.common.utils.PageUtils;
import com.lt.common.utils.Query;
import com.lt.modules.sys.model.dto.message.MessageAddRequest;
import com.lt.modules.sys.model.entity.Message;
import com.lt.modules.sys.model.entity.MessageUser;
import com.lt.modules.sys.service.MessageService;
import com.lt.modules.sys.mapper.MessageMapper;
import com.lt.modules.sys.service.MessageUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author teng
 * @description 针对表【message(消息表)】的数据库操作Service实现
 * @createDate 2022-11-20 16:35:21
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message>
        implements MessageService {

    @Autowired
    private MessageUserService messageUserService;

    @Autowired
    private MessageMapper messageMapper;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String title = (String) params.get("title");
        IPage<Message> page = this.page(
                new Query<Message>().getPage(params),
                new QueryWrapper<Message>()
                        .like(StringUtils.isNotBlank(title), "title", title)
                        .eq("isDelete", 0)
        );
        return new PageUtils(page);
    }

    @Override
    public List<Message> getUserMessage(Long userId) {
        QueryWrapper<MessageUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", userId);
        List<MessageUser> messageUserList = messageUserService.list(queryWrapper);
        return messageUserList.stream().map(messageUser -> {
            Long messageId = messageUser.getMessageId();
            return messageMapper.selectById(messageId);
        }).toList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveMessage(Message message, List<Long> userIds) {
        messageMapper.insert(message);
        Long messageId = message.getId();
        userIds.stream().forEach(userId -> {
            MessageUser messageUser = new MessageUser();
            messageUser.setUserId(userId);
            messageUser.setMessageId(messageId);
            messageUserService.save(messageUser);
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMessage(Message message, List<Long> userIds) {
        // 更新消息
        messageMapper.updateById(message);
        Long messageId = message.getId();
        // 更新消息和用户关联信息
        userIds.stream().forEach(userId -> {
            QueryWrapper<MessageUser> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("messageId", messageId);
            // 先删除
            messageUserService.remove(queryWrapper);
            // 再添加
            MessageUser messageUser = new MessageUser();
            messageUser.setUserId(userId);
            messageUser.setMessageId(messageId);
            messageUserService.save(messageUser);
        });
    }
}




