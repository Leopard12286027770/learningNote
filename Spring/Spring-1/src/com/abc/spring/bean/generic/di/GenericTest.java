package com.abc.spring.bean.generic.di;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

class GenericTest {

	@Test
	void test() {
		ApplicationContext ctx =  new ClassPathXmlApplicationContext("beans-generic-di.xml");
		
		UserService userService = (UserService) ctx.getBean("userService");
		userService.add();
	}

}
