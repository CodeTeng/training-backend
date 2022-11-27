package com.lt.modules.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.lt.common.BaseResponse;
import com.lt.common.ErrorCode;
import com.lt.common.annotation.SysLog;
import com.lt.common.utils.ResultUtils;
import com.lt.modules.sys.model.dto.question.QuestionRequest;
import com.lt.modules.sys.model.entity.Answer;
import com.lt.modules.sys.model.entity.Question;
import com.lt.modules.sys.service.AnswerService;
import com.lt.modules.sys.service.QuestionBankService;
import com.lt.modules.sys.service.QuestionService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2022/11/23 19:01
 */
@RestController
@RequestMapping("/sys/question")
public class QuestionController extends AbstractController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private AnswerService answerService;

    @Autowired
    private QuestionBankService questionBankService;

    /**
     * 获取所有题目信息
     */
    @GetMapping("/getQuestion")
    @RequiresPermissions("sys:question:list")
    public BaseResponse getQuestion(Integer pageNo, Integer pageSize,
                                    @RequestParam(required = false) Integer questionType,
                                    @RequestParam(required = false) Integer questionBankId,
                                    @RequestParam(required = false) String questionContent) {
        if (pageNo == null || pageNo <= 0 || pageSize == null || pageSize <= 0) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "请求参数错误");
        }
        Page<Question> page = questionService.getQuestion(pageNo, pageSize, questionType, questionBankId, questionContent);
        return ResultUtils.success(page);
    }

    /**
     * 根据id获取题目信息
     */
    @GetMapping("/getQuestionById/{questionId}")
    @RequiresPermissions("sys:question:list")
    public BaseResponse getQuestionById(@PathVariable("questionId") Long questionId) {
        if (questionId == null || questionId <= 0) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "参数错误");
        }
        QuestionRequest questionVO = questionService.getQuestionVOById(questionId);
        return ResultUtils.success(questionVO);
    }

    /**
     * 根据题库id和题目类型获取题目信息 type(1单选 2多选 3判断)
     */
    @GetMapping("/getQuestionByBankIdAndType")
    @RequiresPermissions("sys:question:list")
    public BaseResponse getQuestionByBankIdAndType(Long bankId, Integer type) {
        if (bankId == null || bankId <= 0 || type == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "参数错误");
        }
        if (type != 1 && type != 2 && type != 3) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "参数错误");
        }
        List<QuestionRequest> questionVos = questionBankService.getQuestionInfoByBank(bankId);
        questionVos.removeIf(questionRequest -> !Objects.equals(questionRequest.getType(), type));
        return ResultUtils.success(questionVos);
    }

    @PostMapping("/addQuestion")
    @SysLog("添加题目信息")
    @RequiresPermissions("sys:question:save")
    public BaseResponse addQuestion(@RequestBody QuestionRequest questionRequest) {
        Integer type = questionRequest.getType();
        Integer level = questionRequest.getLevel();
        if (type == null || level == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "添加失败,必须填写类型和难度");
        }
        Long[] bankIds = questionRequest.getBankIds();
        if (bankIds == null || bankIds.length <= 0) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "添加失败,必须添加为某个题库");
        }
        String content = questionRequest.getContent();
        if (StringUtils.isBlank(content)) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "添加失败,题目内容不能为空");
        }
        if (type != 4) {
            QuestionRequest.Answer[] answers = questionRequest.getAnswers();
            if (answers == null || answers.length <= 0) {
                return ResultUtils.error(ErrorCode.OPERATION_ERROR, "添加失败,请添加相关题目答案");
            }
            if (type != 2) {
                // 不为多选题 只能添加一个答案
                int count = 0;
                for (int i = 0; i < answers.length; i++) {
                    String isTrue = answers[i].getIsTrue();
                    if ("true".equalsIgnoreCase(isTrue)) {
                        count++;
                    }
                    if (count > 1) {
                        return ResultUtils.error(ErrorCode.OPERATION_ERROR, "不允许添加两个正确答案");
                    }
                }
            }
            for (int i = 0; i < answers.length; i++) {
                String answer = answers[i].getAnswer();
                String analysis = answers[i].getAnalysis();
                if (StringUtils.isAnyBlank(answer, analysis)) {
                    return ResultUtils.error(ErrorCode.OPERATION_ERROR, "添加失败,请添加完整相关答案信息");
                }
            }
        }
        questionRequest.setCreator(getUser().getUsername());
        questionService.addQuestion(questionRequest);
        return ResultUtils.success("添加成功");
    }

    @SysLog("修改试题信息")
    @PostMapping("/updateQuestion")
    @RequiresPermissions("sys:question:update")
    public BaseResponse updateQuestion(@RequestBody QuestionRequest questionRequest) {
        Integer type = questionRequest.getType();
        Integer level = questionRequest.getLevel();
        if (type == null || level == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "修改失败,必须填写类型和难度");
        }
        Long[] bankIds = questionRequest.getBankIds();
        if (bankIds == null || bankIds.length <= 0) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "修改失败,必须添加为某个题库");
        }
        String content = questionRequest.getContent();
        if (StringUtils.isBlank(content)) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "修改失败,题目内容不能为空");
        }
        if (type != 4) {
            QuestionRequest.Answer[] answers = questionRequest.getAnswers();
            if (answers == null || answers.length <= 0) {
                return ResultUtils.error(ErrorCode.OPERATION_ERROR, "修改失败,请添加相关题目答案");
            }
            if (type != 2) {
                // 不为多选题 只能添加一个答案
                int count = 0;
                for (int i = 0; i < answers.length; i++) {
                    String isTrue = answers[i].getIsTrue();
                    if ("true".equalsIgnoreCase(isTrue)) {
                        count++;
                    }
                    if (count > 1) {
                        return ResultUtils.error(ErrorCode.OPERATION_ERROR, "不允许修改为两个正确答案");
                    }
                }
            }
            for (int i = 0; i < answers.length; i++) {
                String answer = answers[i].getAnswer();
                String analysis = answers[i].getAnalysis();
                if (StringUtils.isAnyBlank(answer, analysis)) {
                    return ResultUtils.error(ErrorCode.OPERATION_ERROR, "修改失败,请添加完整相关答案信息");
                }
            }
        }
        questionRequest.setUpdater(getUser().getUsername());
        questionService.updateQuestion(questionRequest);
        return ResultUtils.success("修改成功");
    }

    /**
     * 批量删除题目 多个以,分隔
     */
    @PostMapping("/deleteQuestion")
    @RequiresPermissions("sys:question:delete")
    @SysLog("删除题目")
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse deleteQuestion(String questionIds) {
        if (StringUtils.isBlank(questionIds)) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "参数错误");
        }
        String[] ids = StringUtils.split(questionIds, ",");
        for (String id : ids) {
            // 删除数据库中的题目信息
            questionService.removeById(id);
            // 删除答案表对应的题目答案
            answerService.remove(new QueryWrapper<Answer>().eq("questionId", id));
        }
        return ResultUtils.success("删除成功");
    }

    @PostMapping("/addQuestionToBank")
    @SysLog("增加题库中的题目")
    @RequiresPermissions("sys:question:update")
    public BaseResponse addQuestionToBank(String questionIds, String bankIds) {
        if (StringUtils.isAnyBlank(questionIds, bankIds)) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "参数错误");
        }
        boolean flag = questionService.addQuestionToBank(questionIds, bankIds);
        if (!flag) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "添加失败");
        }
        return ResultUtils.success("添加成功");
    }

    @PostMapping("/removeQuestionFromBank")
    @SysLog("从题库中移除题目")
    @RequiresPermissions("sys:question:update")
    public BaseResponse removeQuestionFromBank(String questionIds, String bankIds) {
        if (StringUtils.isAnyBlank(questionIds, bankIds)) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "参数错误");
        }
        boolean flag = questionService.removeQuestionFromBank(questionIds, bankIds);
        if (!flag) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "删除失败");
        }
        return ResultUtils.success("删除成功");
    }

    /**
     * 根据题目id集合获取题目和答案信息
     */
    @GetMapping("/getQuestionAndAnswerByIds")
    @RequiresPermissions("sys:question:list")
    public BaseResponse getQuestionAndAnswerByIds(String ids) {
        if (StringUtils.isAnyBlank(ids)) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "参数错误");
        }
        String[] questionIds = StringUtils.split(ids, ",");
        if (questionIds == null || questionIds.length == 0) {
            return ResultUtils.success(Lists.newArrayList());
        }
        List<QuestionRequest> questionRequestList = new ArrayList<>();
        for (String questionId : questionIds) {
            QuestionRequest questionRequest = questionService.getQuestionVOById(Long.parseLong(questionId));
            questionRequestList.add(questionRequest);
        }
        questionRequestList.stream().sorted(Comparator.comparingInt(QuestionRequest::getType));
        return ResultUtils.success(questionRequestList);
    }
}
