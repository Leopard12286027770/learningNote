package com.abc.jdbc.tx;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

class TransectionTest {

	private ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
	StoreDao storeDao = ctx.getBean(StoreDao.class);
	Buy buy = ctx.getBean(Buy.class);

	@Test
	void testGetPrice() {
		System.out.println(storeDao.findPriceByDeptId("dev"));
	}
	
	@Test
	void testUpdateNumber() {
		storeDao.decreaseNumber("dev");
	}
	
	@Test
	void testUpdateMoney() {
		storeDao.updateMoney("Peter", 150);;
	}
	
	@Test
	void testBuy() {
		buy.buy("peter", "test");
	}		

}
