package com.lt.modules.sys.model.vo.learn;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @description: 学习情况VO
 * @author: ~Teng~
 * @date: 2022/12/1 15:59
 */
@Data
public class LearnConditionVO implements Serializable {
    private Long id;

    /**
     * 学员id
     */
    private Long userId;

    /**
     * 学员账号
     */
    private String username;

    /**
     * 视频id
     */
    private Long videoId;

    /**
     * 视频标题
     */
    private String videoTitle;

    /**
     * 是否完成 0-未完成 1-完成
     */
    private Integer isDone;

    /**
     * 完成时间
     */
    private Date doneTime;

    /**
     * 视频观看完成度
     */
    private Integer complete;

    /**
     * 创建者
     */
    private String creator;

    /**
     * 创建时间
     */
    private Date createTime;
}
