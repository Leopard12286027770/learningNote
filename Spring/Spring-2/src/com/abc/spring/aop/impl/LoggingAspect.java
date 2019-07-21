package com.abc.spring.aop.impl;

import java.util.Arrays;
import java.util.List;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(2)
public class LoggingAspect {
	
	@Before("execution(* com.abc.spring.aop.impl.Calculator.*(..))")
	public void beforeMethod(JoinPoint joinPoint) {
		String methodName = joinPoint.getSignature().getName();
		List<Object> args = Arrays.asList(joinPoint.getArgs());
		System.out.println("log before method");
		System.out.println(methodName);
		System.out.println(args);
	}
	
	@After("execution(* com.abc.spring.aop.impl.Calculator.*(..))")
	public void afterMethod() {
		System.out.println("log after method");
	}
	
	@AfterReturning(value="execution(* com.abc.spring.aop.impl.Calculator.*(..))",
			returning="result")
	public void afterReturning(JoinPoint joinPoint, Object result) {
		System.out.println("method finished");
		System.out.println("result is : "+result);
	}
	
	@Around("execution(* com.abc.spring.aop.impl.Calculator.add(..))")
	public Object aroundMethod(ProceedingJoinPoint proceedingJoinPoint) {
		System.out.println("Around...........");
		try {
			return proceedingJoinPoint.proceed();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 10000000;
	}	
}
