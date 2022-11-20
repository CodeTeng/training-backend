package com.lt.listener;

import com.lt.modules.sys.controller.MessageController;
import com.lt.modules.sys.model.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @description: 事件监听器
 * @author: ~Teng~
 * @date: 2022/11/20 19:29
 */
@Component
public class MyListener implements ApplicationListener<PublishMessageEvent> {

    @Autowired
    private MessageController messageController;

    @Override
    public void onApplicationEvent(PublishMessageEvent event) {
        List<Long> userIds = event.getUserIds();
        userIds.stream().forEach(userId -> messageController.getUserMessageCount(userId));
    }
}
