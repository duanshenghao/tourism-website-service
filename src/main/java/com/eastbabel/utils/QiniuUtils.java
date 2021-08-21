package com.eastbabel.utils;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;

@Slf4j
@Component
public class QiniuUtils {

    private static String ACCESS_KEY;
    private static String SECRET_KEY;
    private static String BUCKETNAME;
    private static String DOMAIN;

    @Value("${qiniu.ak}")
    public void setAccessKey(String accessKey) {
        ACCESS_KEY = accessKey;
    }
    @Value("${qiniu.sk}")
    public void setSecretKey(String secretKey) {
        SECRET_KEY = secretKey;
    }
    @Value("${qiniu.bucketname}")
    public void setBucketname(String bucketname) {
        BUCKETNAME = bucketname;
    }
    @Value("${qiniu.domain}")
    public void setDomain(String domain) {
        DOMAIN = domain;
    }


    // 密钥
//    private static final Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);

    // 外链默认域名
//    private static final String DOMAIN = "qxo1gxby1.hb-bkt.clouddn.com";

    /**
     * 将图片上传到七牛云
     */
    public static String uploadQNImg(FileInputStream file, String key) {
        // 构造一个带指定Zone对象的配置类, 注意这里的Zone.zone0需要根据主机选择
        Configuration cfg = new Configuration(Zone.zone1());
        // 其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        // 生成上传凭证，然后准备上传

        try {
                Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
            String upToken = auth.uploadToken(BUCKETNAME);
            try {
                Response response = uploadManager.put(file, key, upToken, null, null);
                // 解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);

                String returnPath = putRet.key;
                // 这个returnPath是获得到的外链地址,通过这个地址可以直接打开图片
                return returnPath;
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
