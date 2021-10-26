package com.eastbabel.controller;

import com.eastbabel.bo.Image.ImageEntity;
import com.eastbabel.bo.Image.ImageRes;
import com.eastbabel.bo.base.ResponseEntity;
import com.eastbabel.utils.PictureUtil;
import com.eastbabel.utils.QiniuUtils;
import com.qiniu.util.Auth;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;

@Slf4j
@RestController
@RequestMapping("/qiniu")
@Api(tags = "七牛文件管理")
public class QiNiuController {

    @Value("${qiniu.domain}")
    private String domain;

    @Value("${qiniu.ak}")
    private String accessKey;
    @Value("${qiniu.sk}")
    private String secretKey;


    @ApiOperation("图片上传")
    @RequestMapping(value = "/image", method = RequestMethod.POST)
        public ResponseEntity postUserInforUpDate(MultipartFile multipartFile) throws IOException {
        int begin = multipartFile.getOriginalFilename().lastIndexOf(".");
        int end = multipartFile.getOriginalFilename().length();
        String type = multipartFile.getOriginalFilename().substring(begin,end);
        if (!multipartFile.isEmpty()) {
            FileInputStream inputStream = (FileInputStream) multipartFile.getInputStream();
            String path = QiniuUtils.uploadQNImg(inputStream, "websiteImage/"+PictureUtil.generateRandomFilename()+type); // KeyUtil.genUniqueKey()生成图片的随机名
//            System.out.print("七牛云返回的图片链接:" + path);
            ImageRes imageRes = new ImageRes();
            imageRes.setImageKey(path);

            String fileName = path;
            String domainOfBucket = domain;
            String encodedFileName = URLEncoder.encode(fileName, "utf-8").replace("+", "%20");
            String publicUrl = String.format("%s/%s", domainOfBucket, encodedFileName);
            Auth auth = Auth.create(accessKey, secretKey);
            long expireInSeconds = 31536000;
            String finalUrl = auth.privateDownloadUrl(publicUrl, expireInSeconds);
            System.out.println(finalUrl);
            imageRes.setImageUrl(finalUrl);

            return ResponseEntity.ok(imageRes);
        }
        return ResponseEntity.ok("上传失败");
    }

    @ApiOperation("图片删除")
    @RequestMapping(value = "/delImage",method = RequestMethod.POST)
    public ResponseEntity delImage(@Validated @RequestBody ImageEntity imageEntity){
        QiniuUtils.deleteImage(imageEntity.getImageKey());
        return ResponseEntity.succeed();
    }


}
