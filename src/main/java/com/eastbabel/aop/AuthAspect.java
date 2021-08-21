package com.eastbabel.aop;

import com.eastbabel.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
@Slf4j
public class AuthAspect {
    @Resource
    private WebContext webContext;

    @Value("${token.salt.admin.auth}")
    private String adminTokenSalt;

    @Pointcut("@within(org.springframework.web.bind.annotation.RestController) || @annotation(org.springframework.web.bind.annotation.RestController)")
    public void requestMapping() {

    }

    @Before("requestMapping()")
    public void doBeforeRequestMapping(JoinPoint joinPoint) {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        ServletRequestAttributes attributes = (ServletRequestAttributes) requestAttributes;
        HttpServletRequest request = attributes.getRequest();
        String url = request.getRequestURI();
        if(!(url.contains("list")||url.contains("login")||url.contains("notify/add"))){
            String token = request.getHeader("Authorization");
            TokenUtil.verify(token,adminTokenSalt);
            Integer userId = TokenUtil.getUserId(token);
            webContext.setUserId(userId);
            webContext.setToken(token);
        }
    }

    @After("requestMapping()")
    public void doAfterRequestMapping(JoinPoint joinPoint) {
        webContext.clear();
    }

}
