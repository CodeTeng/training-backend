package com.lt.modules.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lt.common.BaseResponse;
import com.lt.modules.sys.model.entity.ExamRecord;
import com.lt.modules.sys.service.ExamRecordService;
import com.lt.modules.sys.model.entity.User;
import com.lt.modules.sys.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: 考试记录
 * @author: ~Teng~
 * @date: 2022/11/22 10:10
 */
@RestController
@RequestMapping("/sys/examRecord")
public class ExamRecordController extends AbstractController {

    @Autowired
    private ExamRecordService examRecordService;

    @Autowired
    private UserService userService;

    /**
     * 分页查询 考试记录
     */
    @GetMapping("/list")
    public BaseResponse list(Integer pageNo, Integer pageSize,
                             @RequestParam(required = false) Long examId) {
        String username = getUser().getUsername();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(username), "username", username);
        User user = userService.getOne(queryWrapper);
        IPage<ExamRecord> examRecordIPage = new Page<>(pageNo, pageSize);
        return null;
    }

}
