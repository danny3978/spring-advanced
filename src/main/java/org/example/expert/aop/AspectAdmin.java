package org.example.expert.aop;


import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.example.expert.domain.common.annotation.Auth;
import org.hibernate.annotations.Comment;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.net.URLDecoder;
import java.net.http.HttpHeaders;
import java.nio.charset.StandardCharsets;

@Aspect
@Slf4j
@Component
public class AspectAdmin {


    @Pointcut("@annotation(org.example.expert.annotation.AdminLog)")
    private void admin(){}

    @Around("admin()")
    public Object adminLogging(ProceedingJoinPoint joinPoint) throws Throwable{
        long startTime = System.currentTimeMillis();

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        String requestUrl = URLDecoder.decode(request.getRequestURI(), StandardCharsets.UTF_8);
        String user_id = "";
        Object[] args = joinPoint.getArgs();
        if(args.length == 0) log.info("no parameter");
        for(Object arg : args){
            if(arg.getClass().getSimpleName().equals("Long")) {
                user_id = arg.toString();
                break;
            }
        }

        try {
            Object result = joinPoint.proceed();
            return result;
        }finally {
            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;
            log.info("::: Request User_id : {} :::" ,user_id);
            log.info("::: Request URL : {} :::" , requestUrl);
            log.info("::: Time : {}ms :::",executionTime);
        }
    }






}
