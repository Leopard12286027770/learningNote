package com.abc.spring.bean;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.abc.spring.bean.collections.DataSource;
import com.abc.spring.bean.collections.NewPerson;
import com.abc.spring.bean.collections.Person;

public class Main {

	public static void main(String[] args) {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
//		HelloWorld helloWorld = (HelloWorld) applicationContext.getBean("helloWorld");
//		helloWorld.hello();
//		Person person = (Person) ctx.getBean("person");
//		System.out.println(person);
//		Car car= (Car) ctx.getBean("car2");
//		System.out.println(car);
//		Person person =(Person) ctx.getBean("person3");
//		System.out.println(person);
//		NewPerson newPerson = (NewPerson) ctx.getBean("newPerson");
//		System.out.println(newPerson);
//		DataSource dataSource = (DataSource) ctx.getBean("dataSource");
//		System.out.println(dataSource.getProperties());
		Person person =(Person) ctx.getBean("person5");
		System.out.println(person);		
		
	}
}
