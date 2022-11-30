package com.lt.modules.oss.service.impl;

import com.lt.modules.oss.service.OssService;
import com.lt.modules.oss.utils.ConstantProperties;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.region.Region;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.UUID;


/**
 * @description: 图片、视频服务类
 * @author: ~Teng~
 * @date: 2022/11/19 10:29
 */
@Service
public class OssServiceImpl implements OssService {

    private COSClient createCOSClient() {
        String region = ConstantProperties.REGION;
        String secretId = ConstantProperties.SECRET_ID;
        String secretKey = ConstantProperties.SECRET_KEY;

        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setRegion(new Region(region));
        clientConfig.setHttpProtocol(HttpProtocol.https);
        // 设置 socket 读取超时，默认 30s
        clientConfig.setSocketTimeout(30 * 1000);
        // 设置建立连接超时，默认 30s
        clientConfig.setConnectionTimeout(30 * 1000);
        // 生成 cos 客户端。
        return new COSClient(cred, clientConfig);
    }

    @Override
    public String uploadFileAvatar(MultipartFile file) {
        COSClient cosClient = createCOSClient();
        String bucketName = ConstantProperties.BUCKET_NAME;
        String region = ConstantProperties.REGION;
        try {
            // 获取上传文件输入流
            InputStream inputStream = file.getInputStream();
            // 获取上传的文件名
            String fileName = file.getOriginalFilename();
            // 1.1 在文件名称里面添加随机唯一的值
            String uuid = UUID.randomUUID().toString().replace("-", "");
            // 1.2 拼接文件名称 yuy76t5rew01.jpg
            fileName = uuid + fileName;

            // 2.把文件按照日期进行分类
            // 获取当前日期
            String datePath = new DateTime().toString("yyyy/MM/dd");
            // 拼接
            fileName = datePath + "/" + fileName;

            // 调用oss方法实现上传
            // 第一个参数  Bucket名称
            // 第二个参数  上传到oss文件路径和文件名称   aa/bb/1.jpg
            // 第三个参数  上传文件输入流
            ObjectMetadata objectMetadata = new ObjectMetadata();
            cosClient.putObject(bucketName, fileName, inputStream, objectMetadata);

            // 关闭ossClient
            cosClient.shutdown();

            // 把上传之后文件路径返回
            // 需要把上传到阿里云oss路径手动拼接出来
            // https://edu-teng-file.oss-cn-hangzhou.aliyuncs.com/avatar/avatar.png
            return "https://" + bucketName + ".cos." + region + ".myqcloud.com/" + fileName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void deleteObject(String key) {
        COSClient cosClient = createCOSClient();
        String bucketName = ConstantProperties.BUCKET_NAME;
        try {
            cosClient.deleteObject(bucketName, key);
        } catch (CosClientException e) {
            e.printStackTrace();
        }
        cosClient.shutdown();
    }
}
