package com.lt.listener;

import com.lt.modules.sys.model.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @description: 事件发布者
 * @author: ~Teng~
 * @date: 2022/11/20 19:20
 */
@Component
public class MyPublisher {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    /**
     * 事件发布
     */
    public void pushListener(Message message, List<Long> userIds) {
        applicationEventPublisher.publishEvent(new PublishMessageEvent(this, message, userIds));
    }
}
