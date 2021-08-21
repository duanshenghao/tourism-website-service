package com.eastbabel.bo.login;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class LoginReq {
    @NotBlank(message = "登陆账号不能为空")
    private String account;
    @NotBlank(message = "密码不能为空")
    private String password;
}
