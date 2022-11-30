package com.lt.modules.app.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @description: 学习情况请求体
 * @author: ~Teng~
 * @date: 2022/11/30 14:34
 */
@Data
public class StuLearnConditionRequest implements Serializable {
    /**
     * 视频id
     */
    private Long videoId;

    /**
     * 视频观看完成度
     */
    private Integer complete;
}
