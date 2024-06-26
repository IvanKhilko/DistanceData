package org.example.distancedata.aspect;

import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.example.distancedata.services.CounterService;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class LoggingAspect {

    @Pointcut("within(org.example.distancedata.controllers..*)"
            + " || within(org.example.distancedata.services..*)"
            + " || within(org.example.distancedata.cache..*)")
    public void allMethods() {

    }

    @Pointcut("@annotation(AspectAnnotation)")
    public void methodsWithAnnotation() {

    }

    @Pointcut("within(org.example.distancedata.services..*)")
    public void serviceMethods() {

    }

    @Before("serviceMethods()")
    public void logCounterService(final JoinPoint joinPoint) {
        int requestCounter = CounterService.increment();
        log.info("Increment counter from {}.{}()."
                        + " Current value of counter is {}", joinPoint
                        .getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(), requestCounter);
    }

    @Around(value = "methodsWithAnnotation()")
    public Object loggingMethods(final ProceedingJoinPoint point)
            throws Throwable {
        log.info("Enter method {}.{} with argument(s): {}",
                point.getSignature().getDeclaringTypeName(),
                point.getSignature().getName(),
                Arrays.toString(point.getArgs()));
        try {
            Object result = point.proceed();
            log.info("After execution method {}.{} with argument(s): {}",
                    point.getSignature().getDeclaringTypeName(),
                    point.getSignature().getName(),
                    Arrays.toString(point.getArgs()));
            return result;
        } catch (Throwable e) {
            log.error("Illegal argument(s) {} in method {}.{}()",
                    Arrays.toString(point.getArgs()),
                    point.getSignature().getDeclaringTypeName(),
                    point.getSignature().getName());
            throw e;
        }
    }

    @AfterThrowing(pointcut = "allMethods()", throwing = "exception")
    public void logsExceptionsFromAnyLocation(final JoinPoint joinPoint,
                                              final Throwable exception) {
        log.error("Exception in {}.{}() - {}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(), exception.getMessage());
    }
}