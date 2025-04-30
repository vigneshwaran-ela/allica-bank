package com.allica.allica_bank.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

/**
 * Aspect for logging entry and exit of methods in controller and service layers,
 * along with execution time and MDC trace ID.
 */
@Aspect
@Component
public class LoggerAspect {

	private static final Logger logger = LoggerFactory.getLogger(LoggerAspect.class);

    /**
     * Logs method entry, exit, execution time, and trace ID from MDC.
     */
	@Around("controllerExecution() || serviceExecution()")
	public Object loggerAspect(ProceedingJoinPoint joinPoint) throws Throwable {
		
		long start = System.currentTimeMillis();
		String methodName = joinPoint.getSignature().toShortString();
		
		logger.info("Entering into method : {}",  methodName);
		
		Object result = joinPoint.proceed();
		
		long end = System.currentTimeMillis();
		
		logger.info("Exiting from method: {} and total time taken for this in {}ms", methodName, (end-start));
		
		return result;
	}
	
    /**
     * Pointcut for all controller methods in the specified package.
     */
	@Pointcut("execution(* com.allica.allica_bank.controller..*(..))")
	public void controllerExecution() {
		
	}
	
    /**
     * Pointcut for all service methods in the specified package.
     */
	@Pointcut("execution(* com.allica.allica_bank.service..*(..))")
	public void serviceExecution() {
		
	}
}
