package com.lt.modules.sys.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lt.modules.sys.model.entity.LearnCondition;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lt.modules.sys.model.vo.learn.LearnConditionVO;

/**
 * @author teng
 * @description 针对表【learn_condition(学习情况表)】的数据库操作Service
 * @createDate 2022-11-30 14:28:26
 */
public interface LearnConditionService extends IService<LearnCondition> {

    Page<LearnConditionVO> queryPage(Integer pageNo, Integer pageSize, String username, Integer isDone);
}
