package com.eastbabel.controller;

import com.eastbabel.bo.base.PagedResource;
import com.eastbabel.bo.base.ResponseEntity;
import com.eastbabel.bo.email.ToEmail;
import com.eastbabel.bo.notify.CreateNotifyReq;
import com.eastbabel.bo.notify.NotifyBo;
import com.eastbabel.bo.question.QuestionBo;
import com.eastbabel.service.EmailService;
import com.eastbabel.service.NotifyService;
import com.eastbabel.utils.RandomValidateCodeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@Api(tags = "客户垂询接口")
@RequestMapping("api")
public class NotifyController {

    private static final Logger logger = LoggerFactory.getLogger(NotifyController.class);

    @Resource
    private NotifyService notifyService;

    @Resource
    private EmailService emailService;


    @GetMapping("notify/list")
    @ApiOperation("获取垂询列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "status", value = "状态"),
            @ApiImplicitParam(name = "page", value = "页数"),
            @ApiImplicitParam(name = "size", value = "数据条数")
    })
    public ResponseEntity<PagedResource<NotifyBo>> getNotifys(
                                                              @RequestParam(value = "status", required = false) Integer status,
                                                              @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                              @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return ResponseEntity.ok(notifyService.getNotifys(status, page, size));
    }

    @PutMapping("addNotify")
    @ApiOperation("新增垂询")
    public ResponseEntity createNotify(@Validated @RequestBody CreateNotifyReq createNotifyReq){
        notifyService.createNotify(createNotifyReq);
        return ResponseEntity.succeed();
    }

    @DeleteMapping("notify/{id}")
    @ApiOperation("删除垂询")
    public ResponseEntity<String> deleteNotify(@PathVariable("id") Integer id) {
        notifyService.deleteNotify(id);
        return ResponseEntity.ok("success");
    }

    @PutMapping("notify/{id}/status/{status}")
    @ApiOperation("修改垂询状态")
    public ResponseEntity<String> updateNotifyStatus(@PathVariable("id") Integer id,
                                                    @PathVariable("status") Integer status) {
        notifyService.updateNotifyStatus(id, status);
        return ResponseEntity.succeed();
    }

    @PostMapping("notify")
    @ApiOperation("编辑垂询")
    public ResponseEntity<String> editQuestion(@Validated @RequestBody NotifyBo notifyBo) {
        notifyService.editNotify(notifyBo);
        return ResponseEntity.ok("success");
    }

    /**
     * 生成验证码
     */
    @GetMapping(value = "/getVerify")
    @ApiOperation("验证码生成")
    public void getVerify(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("image/jpeg");//设置相应类型,告诉浏览器输出的内容为图片
            response.setHeader("Pragma", "No-cache");//设置响应头信息，告诉浏览器不要缓存此内容
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expire", 0);
            RandomValidateCodeUtil randomValidateCode = new RandomValidateCodeUtil();
            randomValidateCode.getRandcode(request, response);//输出验证码图片方法
        } catch (Exception e) {
            logger.error("获取验证码失败>>>>   ", e);
        }
    }

    @PostMapping(value = "/checkVerify", headers = "Accept=application/json")
    @ApiOperation("验证码校验")
    public boolean checkVerify(@RequestBody Map<String, Object> requestMap, HttpSession session) {
        try{
            //从session中获取随机数
            String inputStr = requestMap.get("inputStr").toString();
            String random = (String) session.getAttribute("RANDOMVALIDATECODEKEY");
            if (random == null) {
                return false;
            }
            if (random.equals(inputStr)) {
                return true;
            } else {
                return false;
            }
        }catch (Exception e){
            logger.error("验证码校验失败", e);
            return false;
        }
    }

}
