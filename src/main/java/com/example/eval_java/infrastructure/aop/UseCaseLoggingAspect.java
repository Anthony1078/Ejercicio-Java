package com.example.eval_java.infrastructure.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class UseCaseLoggingAspect {
    private static final Logger log = LoggerFactory.getLogger(UseCaseLoggingAspect.class);

    @Around("execution(* com.example.eval_java.application..*(..))")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        var start = System.nanoTime();
        try {
            log.info("START {}", pjp.getSignature());
            var ret = pjp.proceed();
            log.info("END {} in {} ms", pjp.getSignature(), (System.nanoTime()-start)/1_000_000);
            return ret;
        } catch (Throwable t) {
            log.error("ERROR {}: {}", pjp.getSignature(), t.toString());
            throw t;
        }
    }
}
