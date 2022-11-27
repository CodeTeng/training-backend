package com.lt.modules.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lt.common.BaseResponse;
import com.lt.common.utils.ResultUtils;
import com.lt.modules.sys.model.entity.Exam;
import com.lt.modules.sys.model.entity.ExamRecord;
import com.lt.modules.sys.service.ExamRecordService;
import com.lt.modules.sys.service.ExamService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @description: 统计控制
 * @author: ~Teng~
 * @date: 2022/11/27 13:32
 */
@RestController
@RequestMapping("/sys/statistics")
public class StatisticsController extends AbstractController {

    @Autowired
    private ExamService examService;

    @Autowired
    private ExamRecordService examRecordService;

    /**
     * 提供每一门考试的通过率数据(echarts绘图)
     */
    @GetMapping("/getExamPassRate")
    @RequiresPermissions("sys:stat:list")
    public BaseResponse getExamPassRate() {
        List<Exam> exams = examService.list();
        List<ExamRecord> examRecords = examRecordService.list(new QueryWrapper<ExamRecord>().isNotNull("totalScore"));
        // 考试名称
        List<String> examNameList = new ArrayList<>();
        // 考试通过率
        List<Double> passRateList = new ArrayList<>();
        double total = 0;
        double pass = 0;
        for (Exam exam : exams) {
            examNameList.add(exam.getExamName());
            total = 0;
            pass = 0;
            for (ExamRecord examRecord : examRecords) {
                if (Objects.equals(examRecord.getExamId(), exam.getId())) {
                    total++;
                    if (examRecord.getTotalScore() >= exam.getPassScore()) {
                        pass++;
                    }
                }
            }
            passRateList.add(pass / total);
        }
        for (int i = 0; i < passRateList.size(); i++) {
            if (Double.isNaN(passRateList.get(i))) {
                passRateList.set(i, 0.0);
            }
        }
        Map<String, String> map = new HashMap<>(2);
        String examNameStr = examNameList.toString();
        String passRateStr = passRateList.toString();
        map.put("examName", examNameStr);
        map.put("passRate", passRateStr);
        return ResultUtils.success(map);
    }

    /**
     * 提供每一门考试的考试次数(echarts绘图)
     */
    @GetMapping("/getExamNumbers")
    @RequiresPermissions("sys:stat:list")
    public BaseResponse getExamNumbers() {
        List<Exam> exams = examService.list();
        List<ExamRecord> examRecords = examRecordService.list();
        // 考试名称
        List<String> examNameList = new ArrayList<>();
        // 考试的考试次数
        List<String> examNumberList = new ArrayList<>();
        int cur = 0;
        for (Exam exam : exams) {
            examNameList.add(exam.getExamName());
            cur = 0;
            for (ExamRecord examRecord : examRecords) {
                if (Objects.equals(examRecord.getExamId(), exam.getId())) {
                    cur++;
                }
            }
            examNumberList.add(cur + "");
        }
        Map<String, String> map = new HashMap<>(2);
        String examNameStr = examNameList.toString();
        String examNumberStr = examNumberList.toString();
        map.put("examName", examNameStr);
        map.put("examNumber", examNumberStr);
        return ResultUtils.success(map);
    }
}
