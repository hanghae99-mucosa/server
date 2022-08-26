package com.hanghae99.mocosa.config.log;


import com.hanghae99.mocosa.config.log.logTrace.ThreadLocalLogTrace;
import com.hanghae99.mocosa.config.log.logTrace.TraceStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Order(value = 2)
@Component
@RequiredArgsConstructor
@Slf4j
public class TraceLogAOP {

    private final ThreadLocalLogTrace threadLocalLogTrace;

    @Pointcut("execution(* com.hanghae99.mocosa.layer..*Controller.*(..))")
    private void allController() {
    }

    @Pointcut("execution(* com.hanghae99.mocosa.layer..*Service.*(..))")
    private void allService() {
    }

    @Pointcut("execution(* com.hanghae99.mocosa.layer..*Repository.*(..))")
    private void allRepository() {
    }

    @Around("allController() || allService() || allRepository()")
    public Object traceLog(ProceedingJoinPoint joinPoint) throws Throwable {
        String massage = joinPoint.getSignature().toShortString();
        TraceStatus status = null;

        try {
            status = threadLocalLogTrace.begin(massage);

            Object result = joinPoint.proceed();

            threadLocalLogTrace.end(status);
            return result;
        } catch (Exception e) {
            threadLocalLogTrace.exception(status, e);
            throw e;
        }
    }
}
