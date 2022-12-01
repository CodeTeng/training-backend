package com.lt.modules.app.service.impl;

import com.lt.common.ErrorCode;
import com.lt.common.exception.BusinessException;
import com.lt.modules.app.service.FaceService;
import com.lt.modules.oss.utils.ConstantProperties;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.iai.v20200303.IaiClient;
import com.tencentcloudapi.iai.v20200303.models.DeletePersonRequest;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2022/12/1 23:22
 */
@Service
public class FaceServiceImpl implements FaceService {
    @Override
    public IaiClient createClient() {
        Credential cred = new Credential(ConstantProperties.SECRET_ID, ConstantProperties.SECRET_KEY);
        // 实例化一个http选项，可选的，没有特殊需求可以跳过
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint("iai.tencentcloudapi.com");
        // 实例化一个client选项，可选的，没有特殊需求可以跳过
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        // 实例化要请求产品的client对象,clientProfile是可选的
        return new IaiClient(cred, ConstantProperties.REGION, clientProfile);
    }

    @Override
    public void deletePerson(Long userId) {
        try {
            IaiClient client = createClient();
            DeletePersonRequest req = new DeletePersonRequest();
            req.setPersonId(String.valueOf(userId));
            client.DeletePerson(req);
        } catch (TencentCloudSDKException e) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, e.getMessage());
        }
    }
}
