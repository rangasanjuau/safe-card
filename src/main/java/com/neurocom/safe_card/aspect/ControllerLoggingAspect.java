package com.neurocom.safe_card.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ControllerLoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(ControllerLoggingAspect.class);

    @Around("within(@org.springframework.web.bind.annotation.RestController *)")
    public Object logControllerExceptions(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            // proceed with the controller method execution
            return joinPoint.proceed();
        } catch (Exception ex) {
            // log details
            log.error("Exception in {}.{}()",
                    joinPoint.getSignature().getDeclaringTypeName(), // class name
                    joinPoint.getSignature().getName(),              // method name
                    ex);                                            // stack trace

            // rethrow so ControllerAdvice can still handle it
            throw ex;
        }
    }
}
