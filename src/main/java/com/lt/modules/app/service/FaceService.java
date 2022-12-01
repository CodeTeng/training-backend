package com.lt.modules.app.service;

import com.tencentcloudapi.iai.v20200303.IaiClient;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2022/12/1 23:22
 */
public interface FaceService {
    IaiClient createClient();

    void deletePerson(Long userId);
}
