package com.lt.modules.oss.service;

import org.springframework.web.multipart.MultipartFile;


/**
 * @description:
 * @author: ~Teng~
 * @date: 2022/11/19 10:28
 */
public interface OssService {

    /**
     * 上传文件到oss
     */
    String uploadFileAvatar(MultipartFile file);
}
