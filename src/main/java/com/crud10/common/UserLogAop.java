package com.crud10.common;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Aspect
@Component
public class UserLogAop {

    @Around( "execution(* com.crud10.domain.member.auth.service.*.*(..))")
    public Object userLog(ProceedingJoinPoint joinPoint) throws Throwable {

        //실행 네서드 정보 문자열로 가져오기
        String pointString = joinPoint.toString();

        // 실행 메서드 로그 기록
        log.info("pointString:{}", pointString);

        // 메서드 실행하기
        Object result = joinPoint.proceed();

        // 성공 메서드 로그 기록
        log.info("result:{}", result);

        return result;
    }

    @AfterReturning(pointcut = " execution(* com.crud10.domain.member.auth.service.AuthService.*.*(..))",returning = "result")
    public void afterReturning(JoinPoint joinPoint, Object result) {

        ObjectMapper objectMapper = new ObjectMapper();

        //실행 네서드 정보 문자열로 가져오기
        String method = joinPoint.toString();

        String writeValueAsString = objectMapper.writeValueAsString(result);

        // 반환 메서드 정보 로그
        log.info("result:{} -> {}", method, writeValueAsString);

    }
}
