package com.lt.modules.sys.model.dto.message;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @description: 发布消息请求体
 * @author: ~Teng~
 * @date: 2022/11/20 18:53
 */
@Data
public class MessageAddRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 消息id
     */
    private Long id;

    /**
     * 消息标题
     */
    @NotBlank(message = "消息标题不能为空")
    private String title;

    /**
     * 消息内容
     */
    @NotBlank(message = "消息内容不能为空")
    private String content;

    /**
     * 收消息者id
     */
    private List<Long> userIds;

    /**
     * 创建者
     */
    private String creator;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updater;

    /**
     * 更新时间
     */
    private Date updateTime;
}
