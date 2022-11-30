package com.lt.modules.app.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @description: 用户消息请求体
 * @author: ~Teng~
 * @date: 2022/11/30 20:29
 */
@Data
public class StuMessageRequest implements Serializable {

    private Long messageId;

    private Integer isRead;
}
