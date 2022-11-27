package com.lt.modules.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lt.common.BaseResponse;
import com.lt.common.ErrorCode;
import com.lt.common.annotation.SysLog;
import com.lt.common.utils.ResultUtils;
import com.lt.modules.sys.model.dto.bank.AddBankRequest;
import com.lt.modules.sys.model.dto.question.QuestionRequest;
import com.lt.modules.sys.model.entity.QuestionBank;
import com.lt.modules.sys.model.vo.bank.BankHaveQuestionSumVO;
import com.lt.modules.sys.service.QuestionBankService;
import com.lt.modules.sys.model.entity.Organ;
import com.lt.modules.sys.service.OrganService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description: 题库管理
 * @author: ~Teng~
 * @date: 2022/11/22 10:46
 */
@RestController
@RequestMapping("/sys/bank")
public class QuestionBankController extends AbstractController {

    @Autowired
    private QuestionBankService questionBankService;

    @Autowired
    private OrganService organService;

    /**
     * 获取所有题库信息
     */
    @GetMapping("/getQuestionBank")
    @RequiresPermissions("sys:bank:list")
    public BaseResponse getQuestionBank() {
        List<QuestionBank> list = questionBankService.list();
        return ResultUtils.success(list);
    }

    /**
     * 获取题库中所有题目类型的数量
     */
    @GetMapping("/getBankHaveQuestionSumByType")
    @RequiresPermissions("sys:bank:list")
    public BaseResponse getBankHaveQuestionSumByType(Integer pageNo, Integer pageSize,
                                                     @RequestParam(required = false) String bankName,
                                                     @RequestParam(required = false) Long organId) {
        if (pageNo == null || pageNo <= 0 || pageSize == null || pageSize <= 0) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "请求参数错误");
        }
        Page<BankHaveQuestionSumVO> page = questionBankService.getBankHaveQuestionSumByType(pageNo, pageSize, bankName, organId);
        return ResultUtils.success(page);
    }

    /**
     * 根据题库id获取题库信息
     */
    @GetMapping("/getQuestionById/{bankId}")
    @RequiresPermissions("sys:bank:list")
    public BaseResponse getQuestionById(@PathVariable("bankId") Long bankId) {
        if (bankId == null || bankId <= 0) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "参数错误");
        }
        QuestionBank questionBank = questionBankService.getById(bankId);
        return ResultUtils.success(questionBank);
    }

    /**
     * 根据题库获取所有的题目信息(单选,多选,判断题)
     */
    @GetMapping("/getQuestionByBank")
    @RequiresPermissions("sys:bank:list")
    public BaseResponse getQuestionByBank(Long bankId) {
        if (bankId == null || bankId <= 0) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "参数错误");
        }
        List<QuestionRequest> questionRequests = questionBankService.getQuestionInfoByBank(bankId);
        return ResultUtils.success(questionRequests);
    }

    @PostMapping("/addQuestionBank")
    @SysLog("添加题库")
    @RequiresPermissions("sys:bank:save")
    public BaseResponse addQuestionBank(@RequestBody @Validated AddBankRequest addBankRequest) {
        String bankName = addBankRequest.getBankName();
        String organName = addBankRequest.getOrganName();
        long count = questionBankService.count(new QueryWrapper<QuestionBank>().eq("bankName", bankName).eq("organName", organName));
        if (count > 0) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "不允许添加重复题库");
        }
        QuestionBank questionBank = new QuestionBank();
        questionBank.setBankName(bankName);
        questionBank.setCreator(getUser().getUsername());
        questionBank.setOrganName(organName);
        Organ organ = organService.getOne(new QueryWrapper<Organ>().eq("name", organName));
        if (organ == null) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "添加失败");
        }
        questionBank.setOrganId(organ.getId());
        boolean flag = questionBankService.save(questionBank);
        if (!flag) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "添加失败");
        }
        return ResultUtils.success(flag);
    }

    @PostMapping("/updateQuestionBank")
    @SysLog("修改题库")
    @RequiresPermissions("sys:bank:update")
    public BaseResponse updateQuestionBank(@RequestBody QuestionBank questionBank) {
        String bankName = questionBank.getBankName();
        String organName = questionBank.getOrganName();
        if (StringUtils.isAnyBlank(bankName, organName)) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "修改失败");
        }
        long count = questionBankService.count(new QueryWrapper<QuestionBank>().eq("bankName", bankName).eq("organName", organName));
        if (count > 0) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "已存在该题库，修改失败");
        }
        Organ organ = organService.getOne(new QueryWrapper<Organ>().eq("name", organName));
        if (organ == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "修改失败");
        }
        questionBank.setUpdater(getUser().getUsername());
        Long organId = organ.getId();
        questionBank.setOrganId(organId);
        boolean flag = questionBankService.updateById(questionBank);
        if (!flag) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "修改失败");
        }
        return ResultUtils.success(flag);
    }

}
