package com.lt.modules.app.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.util.MapUtils;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lt.common.BaseResponse;
import com.lt.common.ErrorCode;
import com.lt.common.annotation.SysLog;
import com.lt.common.exception.BusinessException;
import com.lt.common.utils.ResultUtils;
import com.lt.modules.app.model.dto.StuLearnConditionRequest;
import com.lt.modules.app.model.dto.StuMessageRequest;
import com.lt.modules.app.model.dto.StuRegisterRequest;
import com.lt.modules.app.model.dto.StuUpdateRequest;
import com.lt.modules.app.model.vo.StuAchievement;
import com.lt.modules.app.model.vo.StuExamRecordVO;
import com.lt.modules.app.model.vo.StuVideoVO;
import com.lt.modules.oss.service.OssService;
import com.lt.modules.sys.model.entity.*;
import com.lt.modules.sys.model.vo.organ.OrganVO;
import com.lt.modules.sys.service.*;
import com.lt.modules.app.service.StuService;
import com.lt.modules.sys.controller.AbstractController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @description: 学员前台管理
 * @author: ~Teng~
 * @date: 2022/11/20 20:31
 */
@RestController
@RequestMapping("/app")
public class StuController extends AbstractController {

    @Autowired
    private StuService stuService;

    @Autowired
    private ExamRecordService examRecordService;

    @Autowired
    private ExamService examService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrganService organService;

    @Autowired
    private OrganTypeService organTypeService;

    @Autowired
    private OrganPlanService organPlanService;

    @Autowired
    private LearnConditionService learnConditionService;

    @Autowired
    private VideoService videoService;

    @Autowired
    private MessageUserService messageUserService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private OssService ossService;

    /**
     * 用户注册---需要管理员审核通过后才算真正注册成功
     *
     * @return 新用户的id
     */
    @PostMapping("/register")
    public BaseResponse userRegister(@RequestBody StuRegisterRequest stuRegisterRequest) {
        if (stuRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String username = stuRegisterRequest.getUsername();
        String password = stuRegisterRequest.getPassword();
        String checkPassword = stuRegisterRequest.getCheckPassword();
        String nickname = stuRegisterRequest.getNickname();
        String mobile = stuRegisterRequest.getMobile();
        if (StringUtils.isAnyBlank(username, password, nickname, checkPassword, mobile)) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "请填写完整信息");
        }
        Integer sex = stuRegisterRequest.getSex();
        if (sex == null || (sex != 0 && sex != 1)) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "请填写完整信息");
        }
        if (username.length() < 4) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "用户名最短为4位");
        }
        if (password.length() < 6 || checkPassword.length() < 6) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "密码最短为6位");
        }
        String email = stuRegisterRequest.getEmail();
        if (StringUtils.isNotBlank(email)) {
            boolean isMatch = Pattern.matches("^(\\w+([-.][A-Za-z0-9]+)*){3,18}@\\w+([-.][A-Za-z0-9]+)*\\.\\w+([-.][A-Za-z0-9]+)*$", email);
            if (!isMatch) {
                return ResultUtils.error(ErrorCode.PARAMS_ERROR, "邮箱格式错误");
            }
        }
        long result = stuService.userRegister(stuRegisterRequest);
        return ResultUtils.success(result);
    }

    @PostMapping("/updateMyInfo")
    @SysLog("更新个人信息")
    public BaseResponse updateMyInfo(@RequestBody StuUpdateRequest stuUpdateRequest) {
        String nickname = stuUpdateRequest.getNickname();
        String mobile = stuUpdateRequest.getMobile();
        String avatar = stuUpdateRequest.getAvatar();
        if (StringUtils.isAnyBlank(nickname, mobile, avatar)) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "请填写完整信息");
        }
        String email = stuUpdateRequest.getEmail();
        if (StringUtils.isNotBlank(email)) {
            boolean isMatch = Pattern.matches("^(\\w+([-.][A-Za-z0-9]+)*){3,18}@\\w+([-.][A-Za-z0-9]+)*\\.\\w+([-.][A-Za-z0-9]+)*$", email);
            if (!isMatch) {
                return ResultUtils.error(ErrorCode.PARAMS_ERROR, "邮箱格式错误");
            }
        }
        Integer sex = stuUpdateRequest.getSex();
        if (sex == null || (sex != 0 && sex != 1)) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "请填写完整信息");
        }
        stuUpdateRequest.setUpdater(getUser().getUsername());
        boolean flag = stuService.updateMyInfo(stuUpdateRequest);
        if (!flag) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "更新失败");
        }
        return ResultUtils.success("更新成功");
    }

    @PostMapping("/choiceOrgan")
    @SysLog("用户选择机构")
    public BaseResponse choiceOrgan(Long organId) {
        if (organId == null || organId <= 0) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "参数错误");
        }
        User user = getUser();
        user.setOrganId(organId);
        boolean flag = userService.updateById(user);
        if (!flag) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "选择失败");
        }
        return ResultUtils.success("选择成功");
    }

    /**
     * 查看本人所选择机构
     */
    @GetMapping("/getMyOrgan")
    public BaseResponse getMyOrgan() {
        User user = getUser();
        Long organId = user.getOrganId();
        if (organId == null) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "你尚未选择任何机构");
        }
        Organ organ = organService.getById(organId);
        OrganVO organVO = new OrganVO();
        BeanUtils.copyProperties(organ, organVO);
        OrganType organType = organTypeService.getById(organ.getOrganTypeId());
        organVO.setTypeName(organType.getTypeName());
        return ResultUtils.success(organVO);
    }

    /**
     * 查看本人所选择机构的所有培训计划
     */
    @GetMapping("/getMyOrganPlan")
    public BaseResponse getMyOrganPlan() {
        User user = getUser();
        Long organId = user.getOrganId();
        if (organId == null) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "你尚未选择任何机构，没有相关培训计划");
        }
        List<OrganPlan> organPlanList = organPlanService.list(new QueryWrapper<OrganPlan>().eq("organId", organId));
        return ResultUtils.success(organPlanList);
    }

    /**
     * 记录我的学习情况
     */
    @PostMapping("/recordMyLearnCondition")
    public BaseResponse recordMyLearnCondition(@RequestBody StuLearnConditionRequest learnConditionRequest) {
        if (learnConditionRequest == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "参数错误");
        }
        Long videoId = learnConditionRequest.getVideoId();
        if (videoId == null || videoId <= 0) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "参数错误");
        }
        Integer complete = learnConditionRequest.getComplete();
        if (complete == null || complete < 0 || complete > 100) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "参数错误");
        }
        Long userId = getUserId();
        LearnCondition learnCondition = learnConditionService.getOne(new QueryWrapper<LearnCondition>().eq("userId", userId).eq("videoId", videoId));
        if (learnCondition == null) {
            // 第一次学习该视频 添加
            learnCondition = new LearnCondition();
            learnCondition.setCreator(getUser().getUsername());
            learnCondition.setUserId(userId);
            BeanUtils.copyProperties(learnConditionRequest, learnCondition);
            // 添加
            learnConditionService.save(learnCondition);
        } else {
            // 不是第一次 更新
            learnCondition.setComplete(complete);
            learnCondition.setUpdater(getUser().getUsername());
            if (complete == 100) {
                // 视频完成
                learnCondition.setDoneTime(new Date());
                learnCondition.setIsDone(1);
            }
            // 更新
            learnConditionService.updateById(learnCondition);
        }
        return ResultUtils.success("记录成功");
    }

    /**
     * 查看我的某个培训计划下的所有视频
     */
    @GetMapping("/getMyPlanVideo")
    public BaseResponse getMyPlanVideo(Integer pageNo, Integer pageSize, Long organPlanId, @RequestParam(required = false) Long videoTypeId, @RequestParam(required = false) String videoTitle) {
        User user = getUser();
        Long organId = user.getOrganId();
        if (organId == null) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "你尚未选择任何机构，没有相关培训视频");
        }
        if (pageNo == null || pageNo <= 0 || pageSize == null || pageSize <= 0) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "分页参数错误");
        }
        if (organPlanId == null || organPlanId <= 0) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "参数错误");
        }
        Page<StuVideoVO> page = stuService.getMyPlanVideo(pageNo, pageSize, organPlanId, videoTypeId, videoTitle);
        return ResultUtils.success(page);
    }

    /**
     * 获取个人考试的信息
     */
    @GetMapping("/getMyAllExamInfo")
    public BaseResponse getMyAllExamInfo(Integer pageNo, Integer pageSize, @RequestParam(required = false) String examName, @RequestParam(required = false) String startTime, @RequestParam(required = false) String endTime) {
        if (pageNo == null || pageNo <= 0 || pageSize == null || pageSize <= 0) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "参数错误");
        }
        User user = getUser();
        Long organId = user.getOrganId();
        if (organId == null) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "你尚未加入任何机构，无考试信息");
        }
        Page<Exam> page = examService.getExamInfo(pageNo, pageSize, examName, startTime, endTime, organId);
        return ResultUtils.success(page);
    }

    /**
     * 获取个人考试的信息 不分页
     */
    @GetMapping("/getMyExamList")
    public BaseResponse getMyExamList() {
        User user = getUser();
        Long organId = user.getOrganId();
        if (organId == null) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "你尚未加入任何机构，无考试信息");
        }
        List<Exam> exams = examService.list(new QueryWrapper<Exam>().eq("organId", organId));
        return ResultUtils.success(exams);
    }

    /**
     * 获取个人成绩
     */
    @GetMapping("/getMyGrade")
    public BaseResponse getMyGrade(Integer pageNo, Integer pageSize, @RequestParam(required = false) Long examId) {
        if (pageNo == null || pageNo <= 0 || pageSize == null || pageSize <= 0) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "参数错误");
        }
        User user = getUser();
        Long organId = user.getOrganId();
        if (organId == null) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "你尚未加入任何机构，无考试成绩");
        }
        QueryWrapper<ExamRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", getUserId());
        queryWrapper.eq(examId != null && examId > 0, "examId", examId);
        Page<ExamRecord> page = examRecordService.page(new Page<>(pageNo, pageSize), queryWrapper);
        Page<StuExamRecordVO> resPage = new Page<>();
        BeanUtils.copyProperties(page, resPage);
        // 获取该用户的所有考试记录
        List<ExamRecord> examRecordList = page.getRecords();
        if (examRecordList == null || examRecordList.size() == 0) {
            // 没有考试记录
            return ResultUtils.success(resPage);
        }
        List<StuExamRecordVO> stuExamRecordVOList = examRecordList.stream().map(examRecord -> {
            StuExamRecordVO stuExamRecordVO = new StuExamRecordVO();
            BeanUtils.copyProperties(examRecord, stuExamRecordVO);
            // 获取此次考试
            Exam exam = examService.getById(examRecord.getExamId());
            stuExamRecordVO.setExamName(exam.getExamName());
            Integer passScore = exam.getPassScore();
            Integer logicScore = examRecord.getLogicScore();
            if (logicScore >= passScore) {
                // 考试已经通过
                stuExamRecordVO.setFlag(true);
            }
            Integer totalScore = examRecord.getTotalScore();
            if (totalScore != null) {
                if (totalScore >= passScore) {
                    // 考试也是通过
                    stuExamRecordVO.setFlag(true);
                } else {
                    // 考试未通过
                    stuExamRecordVO.setFlag(false);
                }
            } else {
                // 考试未批阅 简答题没有批
                stuExamRecordVO.setFlag(null);
            }
            return stuExamRecordVO;
        }).toList();
        resPage.setRecords(stuExamRecordVOList);
        return ResultUtils.success(resPage);
    }

    /**
     * 学员成绩单 Excel下载
     */
    @GetMapping("/excelDownload")
    public void excelDownload(HttpServletResponse response) throws IOException {
        String nickname = getUser().getNickname();
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode(nickname + "的成绩单", "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            List<StuAchievement> stuAchievementList = getStuAchievements();
            EasyExcel.write(response.getOutputStream(), StuAchievement.class)
                    .autoCloseStream(Boolean.FALSE).sheet(nickname)
                    .doWrite(stuAchievementList);
        } catch (IOException e) {
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            Map<String, String> map = MapUtils.newHashMap();
            map.put("status", "failure");
            map.put("message", "下载文件失败" + e.getMessage());
            response.getWriter().println(JSON.toJSONString(map));
        }
    }

    private List<StuAchievement> getStuAchievements() {
        List<ExamRecord> examRecordList = examRecordService.list(new QueryWrapper<ExamRecord>().eq("userId", getUserId()));
        List<StuAchievement> stuAchievementList = examRecordList.stream().map(examRecord -> {
            StuAchievement stuAchievement = new StuAchievement();
            BeanUtils.copyProperties(examRecord, stuAchievement);
            // 获取此次考试
            Exam exam = examService.getById(examRecord.getExamId());
            stuAchievement.setExamName(exam.getExamName());
            Integer passScore = exam.getPassScore();
            Integer logicScore = examRecord.getLogicScore();
            if (logicScore >= passScore) {
                // 考试已经通过
                stuAchievement.setIsFlag("通过");
            }
            Integer totalScore = examRecord.getTotalScore();
            if (totalScore != null) {
                if (totalScore >= passScore) {
                    // 考试也是通过
                    stuAchievement.setIsFlag("通过");
                } else {
                    // 考试未通过
                    stuAchievement.setIsFlag("未通过");
                }
            } else {
                // 考试未批阅 简答题没有批
                stuAchievement.setIsFlag("待批阅");
            }
            return stuAchievement;
        }).toList();
        return stuAchievementList;
    }

    /**
     * 学员学习情况统计---视频观看度统计
     */
    @GetMapping("/getStatisticsVideo")
    public BaseResponse getStatisticsVideo() {
        User user = getUser();
        if (user.getOrganId() == null) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "尚未加入任何机构，无统计情况");
        }
        List<LearnCondition> learnConditionList = learnConditionService.list(new QueryWrapper<LearnCondition>().eq("userId", getUserId()));
        if (learnConditionList == null || learnConditionList.size() < 1) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "无任何学习记录");
        }
        List<String> videoTitleList = new ArrayList<>();
        List<Integer> completeList = new ArrayList<>();
        learnConditionList.stream().forEach(learnCondition -> {
            Video video = videoService.getById(learnCondition.getVideoId());
            videoTitleList.add(video.getVideoTitle());
            completeList.add(learnCondition.getComplete());
        });
        Map<String, Object> map = new HashMap<>();
        map.put("videoTitleList", videoTitleList);
        map.put("completeList", completeList);
        return ResultUtils.success(map);
    }

    /**
     * 学员学习情况统计---培训计划完成情况统计
     */
    @GetMapping("/getStatisticsComplete")
    public BaseResponse getStatisticsComplete() {
        User user = getUser();
        if (user.getOrganId() == null) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "尚未加入任何机构，无统计情况");
        }
        List<LearnCondition> learnConditionList = learnConditionService.list(new QueryWrapper<LearnCondition>().eq("userId", getUserId()));
        if (learnConditionList == null || learnConditionList.size() < 1) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "无任何学习记录");
        }
        int count = 0;
        for (LearnCondition learnCondition : learnConditionList) {
            Integer isDone = learnCondition.getIsDone();
            if (isDone == 1) {
                count++;
            }
        }
        List<String> nameList = List.of("完成", "未完成");
        List<Double> completeList = new ArrayList<>();
        double completeDegree = (double) count / learnConditionList.size() * 100;
        BigDecimal bigDecimal1 = new BigDecimal(String.valueOf(completeDegree)).setScale(2, RoundingMode.HALF_UP);
        BigDecimal bigDecimal2 = new BigDecimal("100.00").subtract(bigDecimal1);
        completeList.add(bigDecimal1.doubleValue());
        completeList.add(bigDecimal2.doubleValue());
        Map<String, Object> map = new HashMap<>(2);
        map.put("nameList", nameList);
        map.put("completeList", completeList);
        return ResultUtils.success(map);
    }

    /**
     * 统计每门考试分数
     */
    @GetMapping("/getStatisticsScore")
    public BaseResponse getStatisticsScore() {
        User user = getUser();
        if (user.getOrganId() == null) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "尚未加入任何机构，无统计情况");
        }
        List<ExamRecord> examRecordList = examRecordService.list(new QueryWrapper<ExamRecord>().eq("userId", getUserId()));
        if (examRecordList == null || examRecordList.size() < 1) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "暂未考试成绩");
        }
        List<Integer> totalScoreList = new ArrayList<>();
        List<String> examNameList = new ArrayList<>();
        List<Integer> passScoreList = new ArrayList<>();
        examRecordList.stream().filter(examRecord -> examRecord.getTotalScore() != null).forEach(examRecord -> {
            Integer totalScore = examRecord.getTotalScore();
            totalScoreList.add(totalScore);
            Exam exam = examService.getById(examRecord.getExamId());
            examNameList.add(exam.getExamName());
            passScoreList.add(exam.getPassScore());
        });
        Map<String, Object> map = new HashMap<>();
        map.put("examNameList", examNameList);
        map.put("passScoreList", passScoreList);
        map.put("totalScoreList", totalScoreList);
        return ResultUtils.success(map);
    }

    /**
     * 统计学员未读的消息个数
     */
    @GetMapping("/getMyMessageNoReadCount")
    public BaseResponse getMyMessageNoReadCount() {
        Long userId = getUserId();
        long count = messageUserService.count(new QueryWrapper<MessageUser>().eq("userId", userId).eq("isRead", 0));
        return ResultUtils.success(count);
    }

    /**
     * 获取学员所有消息
     */
    @GetMapping("/getMyAllMessage")
    public BaseResponse getMyAllMessage() {
        Long userId = getUserId();
        List<MessageUser> messageUserList = messageUserService.list(new QueryWrapper<MessageUser>().eq("userId", userId));
        if (messageUserList == null || messageUserList.size() == 0) {
            return ResultUtils.success("未收到消息");
        }
        List<Long> messageIdList = messageUserList.stream().map(MessageUser::getMessageId).toList();
        List<Message> messageList = messageService.list(new QueryWrapper<Message>().in("id", messageIdList));
        return ResultUtils.success(messageList);
    }

    /**
     * 设置消息已读
     */
    @PostMapping("/setRead")
    public BaseResponse setRead(@RequestBody StuMessageRequest stuMessageRequest) {
        if (stuMessageRequest == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "参数错误");
        }
        MessageUser messageUser = new MessageUser();
        BeanUtils.copyProperties(stuMessageRequest, messageUser);
        messageUserService.update(messageUser, new UpdateWrapper<MessageUser>().eq("userId", getUserId()).eq("messageId", messageUser.getMessageId()));
        return ResultUtils.success("消息已读");
    }

    /**
     * 学员退出机构
     */
    @PostMapping("/signOutOrgan")
    @SysLog("学员退出机构")
    public BaseResponse signOutOrgan() {
        User user = getUser();
        Long organId = user.getOrganId();
        if (organId == null) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "未加入机构，无法退出");
        }
        // 所有的视频要全部完成才能退出
        List<LearnCondition> learnConditionList = learnConditionService.list(new QueryWrapper<LearnCondition>().eq("userId", user.getId()));
        for (LearnCondition learnCondition : learnConditionList) {
            Integer isDone = learnCondition.getIsDone();
            if (isDone != 1) {
                return ResultUtils.error(ErrorCode.OPERATION_ERROR, "培训计划视频未完成，无法退出机构");
            }
        }
        // 所有考试通过才能退出
        List<ExamRecord> examRecordList = examRecordService.list(new QueryWrapper<ExamRecord>().eq("userId", user.getId()));
        // 获取所有考试id集合
        for (ExamRecord examRecord : examRecordList) {
            Long examId = examRecord.getExamId();
            Exam exam = examService.getById(examId);
            Integer totalScore = examRecord.getTotalScore();
            Integer logicScore = examRecord.getLogicScore();
            Integer passScore = exam.getPassScore();
            if (logicScore < passScore) {
                return ResultUtils.error(ErrorCode.OPERATION_ERROR, "考试未全部通过，无法退出机构");
            } else {
                if (totalScore == null) {
                    // 老师没有批阅 无法退出机构
                    return ResultUtils.error(ErrorCode.OPERATION_ERROR, "考试未全部通过，无法退出机构");
                } else if (totalScore < passScore) {
                    // 考试未通过
                    return ResultUtils.error(ErrorCode.OPERATION_ERROR, "考试未全部通过，无法退出机构");
                }
            }
        }
        // 所有考试也全部通过 申请退出
        user.setOrganId(null);
        user.setUpdater(getUser().getUsername());
        userService.updateById(user);
        return ResultUtils.success("退出成功");
    }
}
