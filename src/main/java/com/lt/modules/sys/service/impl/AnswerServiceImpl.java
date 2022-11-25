package com.lt.modules.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.modules.sys.model.entity.Answer;
import com.lt.modules.sys.service.AnswerService;
import com.lt.modules.sys.mapper.AnswerMapper;
import org.springframework.stereotype.Service;

/**
 * @author teng
 * @description 针对表【answer(答案表)】的数据库操作Service实现
 * @createDate 2022-11-22 09:24:12
 */
@Service
public class AnswerServiceImpl extends ServiceImpl<AnswerMapper, Answer>
        implements AnswerService {

}




