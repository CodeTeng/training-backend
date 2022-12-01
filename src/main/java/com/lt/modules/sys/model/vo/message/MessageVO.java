package com.lt.modules.sys.model.vo.message;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @description: 消息视图
 * @author: ~Teng~
 * @date: 2022/12/1 15:37
 */
@Data
public class MessageVO implements Serializable {
    /**
     * 消息id
     */
    private Long id;

    /**
     * 消息标题
     */
    private String title;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 学员id
     */
    private Long userId;

    /**
     * 学员账号
     */
    private String username;

    /**
     * 是否已读 0-未读 1-已读
     */
    private Integer isRead;

    /**
     * 创建者
     */
    private String creator;

    /**
     * 创建时间
     */
    private Date createTime;
}
