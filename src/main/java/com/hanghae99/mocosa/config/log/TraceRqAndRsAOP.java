package com.hanghae99.mocosa.config.log;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.json.JSONObject;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Order(value = 1)
@Component
@RequiredArgsConstructor
@Slf4j
public class TraceRqAndRsAOP {
    @Pointcut("execution(* com.hanghae99.mocosa.layer..*Controller.*(..))")
    private void allController() {
    }

    @Around("allController()")
    public Object traceRqAndRs(ProceedingJoinPoint joinPoint) throws Throwable {
        //request Log 를 위한 로직
        Object[] request = joinPoint.getArgs();

        Method method = getMethod(joinPoint);

        JSONObject rqLogJson = new JSONObject();
        for (Object rq : request) {
            rqLogJson.put(method.getName(), rq.toString());
        }
        String logRq = rqLogJson.toString();

        log.info("request ={}", logRq);

        Object rs = joinPoint.proceed();
        //response Log 를 위한 로직

        JSONObject rsLogJson = new JSONObject();
        rsLogJson.put(method.getName(), rs.toString());
        String logRr = rsLogJson.toString();

        log.info("response ={}",logRr);
        return rs;
    }

    private Method getMethod(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getMethod();
    }
}
