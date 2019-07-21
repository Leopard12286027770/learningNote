package com.abc.spring.bean.annotation;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.abc.spring.bean.annotation.repository.UserRepository;

public class AnnotationTest {

	public static void main(String[] args) {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("beans-annotation.xml");
		TestObject tObject = (TestObject) ctx.getBean("testObject");
		System.out.println(tObject);
		
		UserRepository ur = (UserRepository) ctx.getBean("userRepo");
		ur.save();
	}

}
