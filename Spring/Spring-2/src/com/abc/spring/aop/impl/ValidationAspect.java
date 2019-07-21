package com.abc.spring.aop.impl;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Order(1)
public class ValidationAspect {
	@Pointcut("execution(* com.abc.spring.aop.impl.Calculator.*(..))")
	public void declareJoinPointExpression() {
		
	}
	
//	@Before("declareJoinPointExpression()")
	@Before("execution(* com.abc.spring.aop.impl.Calculator.*(..))")
	public void validator(JoinPoint joinPoint) {
		System.out.println("validate: "+Arrays.asList(joinPoint.getArgs()));
	}
}
