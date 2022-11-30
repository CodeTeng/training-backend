package com.lt.modules.app.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @description: 前台用户视频视图
 * @author: ~Teng~
 * @date: 2022/11/30 13:36
 */
@Data
public class StuVideoVO implements Serializable {
    /**
     * 视频id
     */
    private Long id;

    /**
     * 视频标题
     */
    private String videoTitle;

    /**
     * 视频封面地址
     */
    private String coverUrl;

    /**
     * 视频地址
     */
    private String videoUrl;

    /**
     * 创建者
     */
    private String creator;

    /**
     * 创建时间
     */
    private Date createTime;
}
