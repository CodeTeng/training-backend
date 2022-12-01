package com.lt.modules.sys.model.vo.video;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @description: 视频视图
 * @author: ~Teng~
 * @date: 2022/12/1 13:55
 */
@Data
public class VideoVO implements Serializable {
    private Long id;

    /**
     * 培训计划id
     */
    private Long organPlanId;

    /**
     * 机构名称
     */
    private String organName;

    /**
     * 视频类别id
     */
    private Long videoTypeId;

    /**
     * 视频类型名称
     */
    private String typeName;

    private String videoTitle;

    private String coverUrl;

    private String videoUrl;

    /**
     * 视频状态 0-正常 1-停用 2-审核
     */
    private Integer status;

    /**
     * 是否发布 0-发布 1-未发布
     */
    private Integer isPublish;

    private String creator;

    private Date createTime;

    private String updater;

    private Date updateTime;
}
