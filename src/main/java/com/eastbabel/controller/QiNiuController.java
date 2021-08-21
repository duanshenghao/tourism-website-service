package com.eastbabel.controller;

import com.eastbabel.bo.base.ResponseEntity;
import com.eastbabel.utils.PictureUtil;
import com.eastbabel.utils.QiniuUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
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
            return ResponseEntity.ok(path);
        }
        return ResponseEntity.ok("上传失败");
    }
}
