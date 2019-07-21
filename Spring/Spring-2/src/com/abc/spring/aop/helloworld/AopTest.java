package com.abc.spring.aop.helloworld;

public class AopTest {

	public static void main(String[] args) {
		Calculator target = new CalculatorImpl();
		Calculator proxy = new LogginProxy(target).getLoggingProxy();
		System.out.println(proxy.add(1, 2));
	}

}
