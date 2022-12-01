package com.lt.modules.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lt.common.BaseResponse;
import com.lt.common.ErrorCode;
import com.lt.common.annotation.SysLog;
import com.lt.common.utils.PageUtils;
import com.lt.common.utils.ResultUtils;
import com.lt.listener.MyPublisher;
import com.lt.modules.sys.model.dto.message.MessageAddRequest;
import com.lt.modules.sys.model.entity.Message;
import com.lt.modules.sys.model.entity.MessageUser;
import com.lt.modules.sys.model.vo.message.MessageVO;
import com.lt.modules.sys.service.MessageService;
import com.lt.modules.sys.service.MessageUserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 * @description: 消息管理
 * @author: ~Teng~
 * @date: 2022/11/20 16:39
 */
@RestController
@RequestMapping("/sys/message")
public class MessageController extends AbstractController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private MessageUserService messageUserService;

    @Autowired
    private MyPublisher myPublisher;

    /**
     * 分页查询
     */
    @RequestMapping("/list")
    @RequiresPermissions("sys:message:list")
    public BaseResponse queryPage(Integer pageNo, Integer pageSize,
                                  @RequestParam(required = false) String title) {
        Page<Message> page = messageService.queryPage(pageNo, pageSize, title);
        return ResultUtils.success(page);
    }

    /**
     * 所有消息
     */
    @GetMapping("/select")
    @RequiresPermissions("sys:message:select")
    public BaseResponse select() {
        List<Message> list = messageService.list();
        return ResultUtils.success(list);
    }

    /**
     * 单个消息
     */
    @GetMapping("/info/{id}")
    @RequiresPermissions("sys:message:info")
    public BaseResponse info(@PathVariable("id") Long id) {
        if (id == null || id <= 0) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "请求参数错误");
        }
        Message message = messageService.getById(id);
        if (message == null) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "查询失败");
        }
        return ResultUtils.success(message);
    }

    /**
     * 获取某个学员的所有消息
     */
    @GetMapping("/select/{userId}")
    @RequiresPermissions("sys:message:select")
    public BaseResponse getUserMessage(@PathVariable("userId") Long userId) {
        if (userId == null || userId <= 0) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "请求参数错误");
        }
        List<Message> messageList = messageService.getUserMessage(userId);
        return ResultUtils.success(messageList);
    }

    /**
     * 学员收到的消息个数
     */
    @GetMapping("/count/{userId}")
    @RequiresPermissions("sys:message:select")
    public BaseResponse getUserMessageCount(@PathVariable("userId") Long userId) {
        if (userId == null || userId <= 0) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "请求参数错误");
        }
        QueryWrapper<MessageUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", userId);
        long count = messageUserService.count(queryWrapper);
        return ResultUtils.success(count);
    }

    @PostMapping("/save")
    @SysLog("发布消息")
    @RequiresPermissions("sys:message:save")
    public BaseResponse save(@RequestBody @Validated MessageAddRequest messageAddRequest) {
        List<Long> userIds = messageAddRequest.getUserIds();
        if (userIds == null || userIds.size() <= 0) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "收消息者不能为空");
        }
        Message message = new Message();
        BeanUtils.copyProperties(messageAddRequest, message);
        message.setCreator(getUser().getUsername());
        messageService.saveMessage(message, userIds);
//        myPublisher.pushListener(message, userIds);
        return ResultUtils.success(true);
    }

    @PostMapping("/update")
    @SysLog("修改消息")
    @RequiresPermissions("sys:message:update")
    public BaseResponse update(@RequestBody @Validated MessageAddRequest messageAddRequest) {
        Message message = new Message();
        BeanUtils.copyProperties(messageAddRequest, message);
        message.setUpdater(getUser().getUsername());
        List<Long> userIds = messageAddRequest.getUserIds();
        messageService.updateMessage(message, userIds);
        return ResultUtils.success(true);
    }

    @PostMapping("/delete/{id}")
    @SysLog("删除消息")
    @RequiresPermissions("sys:message:delete")
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse delete(@PathVariable("id") Long id) {
        if (id == null || id <= 0) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "请求参数错误");
        }
        // 删除消息
        Message message = messageService.getById(id);
        message.setUpdater(getUser().getUsername());
        messageService.updateById(message);
        boolean flag = messageService.removeById(id);
        if (!flag) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "删除失败");
        } else {
            // 删除关联信息
            QueryWrapper<MessageUser> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("messageId", id);
            boolean remove = messageUserService.remove(queryWrapper);
            if (!remove) {
                return ResultUtils.error(ErrorCode.OPERATION_ERROR, "删除失败");
            } else {
                return ResultUtils.success(remove);
            }
        }
    }
}
