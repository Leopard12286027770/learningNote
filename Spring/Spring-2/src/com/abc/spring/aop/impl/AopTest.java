package com.abc.spring.aop.impl;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AopTest {
	public static void main(String[] args) {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		Calculator calculator = (Calculator) ctx.getBean(Calculator.class);
		int result = calculator.add(5, 10);
		System.out.println(result);
		System.out.println(calculator.div(5, 2));
		
	}
}
