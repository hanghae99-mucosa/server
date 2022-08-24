package com.hanghae99.mocosa.config.log;


import com.hanghae99.mocosa.config.log.annotation.DelayLog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;


@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class TraceDelay {

    @Around("@annotation(delayLog)")
    public Object catchDelay(ProceedingJoinPoint joinPoint, DelayLog delayLog) throws Throwable {
        long starTime = System.currentTimeMillis();

        Object proceed = joinPoint.proceed();
        long endTime = System.currentTimeMillis();

        long result = endTime - starTime;

        if (result >= delayLog.value()){
            log.warn("기준 처리속도 이상, 기준 속도={}, 현재 처리 속도={}", delayLog.value(), result);
        }
        return proceed;
    }
}
