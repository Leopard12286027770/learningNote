package com.abc.spring.bean.properties;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.abc.spring.bean.autowire.Car;


public class PropertiesTest {
	public static void main(String[] args) throws SQLException {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("beans-properties.xml");
		DataSource dataSource =  (DataSource) ctx.getBean("dataSource");
		System.out.println(dataSource.getConnection());
	}
}
