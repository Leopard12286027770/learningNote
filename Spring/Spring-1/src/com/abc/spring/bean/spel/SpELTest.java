package com.abc.spring.bean.spel;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpELTest {
	public static void main(String[] args) {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("beans-spel.xml");
//		Car car = (Car) ctx.getBean("car");
//		System.out.println(car);
		Person person= (Person) ctx.getBean("person");
		System.out.println(person);
		
	}
}
