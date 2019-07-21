package com.abc.spring.aop.helloworld;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

public class LogginProxy {
	private Calculator target;
	
	public LogginProxy() {
		super();
	}
	
	public LogginProxy(Calculator target) {
		super();
		this.target = target;
	}

	public Calculator getLoggingProxy() {
		Calculator proxy = null;
		ClassLoader loader = target.getClass().getClassLoader();
		Class[] interfaces = new Class[] {Calculator.class};
		InvocationHandler h= new InvocationHandler() {
			
			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				System.out.println("Inside Proxy, invoking....."+" method:"
						+ method.getName()+"  args: "+ Arrays.asList(args));
				Object result = method.invoke(target, args);
				System.out.println("result:  "+result);
				return 0;
			}
		};
		proxy=(Calculator) Proxy.newProxyInstance(loader, interfaces, h);
		
		return proxy;
	}
}
