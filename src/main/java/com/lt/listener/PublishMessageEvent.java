package com.lt.listener;

import com.lt.modules.sys.model.entity.Message;
import org.springframework.context.ApplicationEvent;

import java.io.Serializable;
import java.util.List;

/**
 * @description: 自定义事件
 * @author: ~Teng~
 * @date: 2022/11/20 19:16
 */
public class PublishMessageEvent extends ApplicationEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    private Message message;

    private List<Long> userIds;

    public PublishMessageEvent(Object source, Message message, List<Long> userIds) {
        super(source);
        this.message = message;
        this.userIds = userIds;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public List<Long> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Long> userIds) {
        this.userIds = userIds;
    }
}
