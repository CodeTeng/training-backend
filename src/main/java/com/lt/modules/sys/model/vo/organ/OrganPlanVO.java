package com.lt.modules.sys.model.vo.organ;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @description: 机构培训视图
 * @author: ~Teng~
 * @date: 2022/11/19 21:16
 */
@Data
public class OrganPlanVO implements Serializable {

    /**
     * 培训计划id
     */
    private Long id;

    /**
     * 机构名称
     */
    private String name;

    /**
     * 培训内容
     */
    private String content;

    /**
     * 培训开始时间
     */
    private Date startTime;

    /**
     * 培训周期 默认7天
     */
    private Integer trainPeriod;

    /**
     * 培训结束时间 开始时间+培训周期
     */
    private Date endTime;

    private static final long serialVersionUID = 1L;
}
