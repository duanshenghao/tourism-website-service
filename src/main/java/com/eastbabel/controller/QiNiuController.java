package com.eastbabel.controller;

import com.eastbabel.bo.Image.ImageEntity;
import com.eastbabel.bo.Image.ImageRes;
import com.eastbabel.bo.base.ResponseEntity;
import com.eastbabel.utils.PictureUtil;
import com.eastbabel.utils.QiniuUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.FileInputStream;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/qiniu")
@Api(tags = "七牛文件管理")
public class QiNiuController {

    @Value("${qiniu.domain}")
    private String domain;


    @ApiOperation("图片上传")
    @RequestMapping(value = "/image", method = RequestMethod.POST)
    public ResponseEntity postUserInforUpDate(HttpServletRequest request, @RequestParam("file") MultipartFile multipartFile) throws IOException {

        // 用来获取其他参数
//        MultipartHttpServletRequest params = ((MultipartHttpServletRequest) request);
//        String name = params.getParameter("username");
        if (!multipartFile.isEmpty()) {
            FileInputStream inputStream = (FileInputStream) multipartFile.getInputStream();
            String path = QiniuUtils.uploadQNImg(inputStream, "websiteImage/"+PictureUtil.generateRandomFilename()); // KeyUtil.genUniqueKey()生成图片的随机名
            System.out.print("七牛云返回的图片链接:" + path);
            ImageRes imageRes = new ImageRes();
            imageRes.setImageKey(path);
            imageRes.setImageUrl(domain+path);
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
