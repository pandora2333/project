package com.dangdangnet.goods.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * 专注于购物的切面web日志
 * @author pandora2333
 */
@Aspect
@Component
@Slf4j
public class WebLog {

    @Pointcut("execution(public * com.dangdangnet.goods.controller.*.*(..))")
    public void webLog(){
    }
    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) {
        //代理的是哪一个方法
        Signature signature = joinPoint.getSignature();
        log.info("方法：{}",signature.getName());
        //获取目标方法的参数信息
        Object[] obj = joinPoint.getArgs();
        //AOP代理类的名字
        log.info("方法所在包:{}",signature.getDeclaringTypeName());
        //AOP代理类的类（class）信息
        signature.getDeclaringType();
        MethodSignature methodSignature = (MethodSignature) signature;
        String[] strings = methodSignature.getParameterNames();
        log.info("参数名：{}", Arrays.toString(strings));
        log.info("参数值ARGS : {}", Arrays.toString(joinPoint.getArgs()));
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest req = attributes.getRequest();
        // 记录下请求内容
        log.info("请求URL :{} ",req.getRequestURL().toString());
        log.info("HTTP_METHOD : {}",req.getMethod());
        log.info("IP : {}",req.getRemoteAddr());
        log.info("CLASS_METHOD : {}",joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
    }

}
