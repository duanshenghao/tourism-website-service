package com.eastbabel.service.impl;

import com.eastbabel.aop.WebContext;
import com.eastbabel.bo.login.TokenBo;
import com.eastbabel.dao.entity.SysUser;
import com.eastbabel.dao.repository.UserRepository;
import com.eastbabel.exception.CustomException;
import com.eastbabel.service.LoginService;
import com.eastbabel.utils.PasswordUtil;
import com.eastbabel.utils.TokenUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class LoginServiceImpl implements LoginService {

    @Resource
    private UserRepository userRepository;

    @Value("${token.salt.admin.auth}")
    private String adminTokenSalt;

    @Value("${token.salt.admin.refresh}")
    private String adminRefreshTokenSalt;

    @Resource
    private WebContext webContext;

    @Override
    public TokenBo adminLogin(String account, String password) {
        if(StringUtils.isEmpty(account)){
            new CustomException("用户名不能为空");
        }
        if(StringUtils.isEmpty(password)){
            new CustomException("密码不能为空");
        }
        //去数据库拿密码验证用户名密码
        SysUser user = userRepository.findByUserName(account);
        // 用户不存在或者密码错误
        new CustomException("账号或密码错误").throwIf(user == null);
        new CustomException("请联系管理员初始化密码").throwIf(StringUtils.isBlank(user.getPassword()));
        String securityPwd = PasswordUtil.encryption(password, user.getSalt());
        new CustomException("账号或密码错误").throwIf(!securityPwd.equals(user.getPassword()));
        TokenBo token = TokenUtil.signTokens(user.getId(),adminTokenSalt, adminRefreshTokenSalt);
        webContext.setUserId(user.getId());
        webContext.setToken(token.getAuthToken());
        return token;
    }

}
