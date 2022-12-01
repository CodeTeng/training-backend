package com.lt.modules.app.controller;

import com.lt.common.BaseResponse;
import com.lt.common.ErrorCode;
import com.lt.common.annotation.SysLog;
import com.lt.common.exception.BusinessException;
import com.lt.common.utils.ResultUtils;
import com.lt.modules.app.service.FaceService;
import com.lt.modules.oss.utils.ConstantProperties;
import com.lt.modules.sys.controller.AbstractController;
import com.lt.modules.sys.model.entity.User;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.iai.v20200303.IaiClient;
import com.tencentcloudapi.iai.v20200303.models.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: 人脸控制器
 * @author: ~Teng~
 * @date: 2022/12/1 20:37
 */
@RestController
@RequestMapping("/app/face")
public class FaceController extends AbstractController {

    /**
     * 管理员人员库id
     */
    private static final String ADMIN_GROUP_ID = "0001";

    /**
     * 普通学员人员库id
     */
    private static final String STU_GROUP_ID = "0002";

    @Autowired
    private FaceService faceService;

    @PostMapping("/faceVerification")
    @SysLog("人员验证")
    public BaseResponse faceVerification(String url) {
        Long userId = getUserId();
        try {
            IaiClient client = faceService.createClient();
            VerifyPersonRequest request = new VerifyPersonRequest();
            request.setUrl(url);
            request.setPersonId(String.valueOf(userId));
            VerifyPersonResponse response = client.VerifyPerson(request);
            Float score = response.getScore();
            BigDecimal decimal = new BigDecimal(String.valueOf(score)).setScale(2, RoundingMode.HALF_UP);
            Boolean isMatch = response.getIsMatch();
            Map<String, Object> map = new HashMap<>(2);
            map.put("score", decimal.floatValue());
            map.put("isMatch", isMatch);
            return ResultUtils.success(map);
        } catch (TencentCloudSDKException e) {
            String message = e.getMessage();
            throw new BusinessException(ErrorCode.OPERATION_ERROR, message);
        }
    }

    /**
     * 用户本人注册时上传头像，添加人员至腾讯云
     */
    @PostMapping("/createPerson")
    public BaseResponse createPerson(String url) {
        User user = getUser();
        if (StringUtils.isBlank(url)) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "请上传本人头像");
        }
        try {
            IaiClient client = faceService.createClient();
            CreatePersonRequest req = new CreatePersonRequest();
            req.setGroupId(STU_GROUP_ID);
            req.setPersonName(user.getNickname());
            req.setPersonId(String.valueOf(getUserId()));
            req.setGender((long) (user.getSex() == 0 ? 1 : 2));
            req.setUrl(url);
            client.CreatePerson(req);
            return ResultUtils.success("添加人脸成功");
        } catch (TencentCloudSDKException e) {
            String message = e.getMessage();
            throw new BusinessException(ErrorCode.OPERATION_ERROR, message);
        }
    }

    @PostMapping("/createFace")
    @SysLog("用户增加人脸")
    public BaseResponse createFace(String[] urls) {
        try {
            IaiClient client = faceService.createClient();
            CreateFaceRequest req = new CreateFaceRequest();
            req.setPersonId(String.valueOf(getUserId()));
            req.setUrls(urls);
            client.CreateFace(req);
            return ResultUtils.success("添加人脸成功");
        } catch (TencentCloudSDKException e) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, e.getMessage());
        }
    }
}
