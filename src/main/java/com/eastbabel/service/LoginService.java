package com.eastbabel.service;


import com.eastbabel.bo.login.TokenBo;

public interface LoginService {
    TokenBo adminLogin(String account, String password);

}
