package com.eastbabel.controller;


import com.eastbabel.bo.base.ResponseEntity;
import com.eastbabel.bo.login.LoginReq;
import com.eastbabel.bo.login.TokenBo;
import com.eastbabel.service.LoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@Api(tags = "登录接口")
@RequestMapping("api")
public class LoginController {

    @Resource
    private LoginService loginService;

    @PostMapping("login")
    @ApiOperation("管理员登陆")
    public ResponseEntity<TokenBo> adminLogin(@RequestBody @Valid LoginReq adminLoginReq) {
        TokenBo tokenBo = loginService.adminLogin(adminLoginReq.getAccount(), adminLoginReq.getPassword());
        return ResponseEntity.ok(tokenBo);
    }
}
