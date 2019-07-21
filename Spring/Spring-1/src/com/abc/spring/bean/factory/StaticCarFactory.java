package com.abc.spring.bean.factory;

import java.util.HashMap;
import java.util.Map;

public class StaticCarFactory {
	
	private static Map<String, Car> cars= new HashMap<>();
	
	static {
		cars.put("audi", new Car("audi",30000.0));
		cars.put("bmw", new Car("BMW",80000.0));
	}
	public static Car getCar(String name) {
		return cars.get(name);
	}
}
