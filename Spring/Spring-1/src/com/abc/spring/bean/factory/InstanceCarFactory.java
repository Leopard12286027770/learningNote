package com.abc.spring.bean.factory;

import java.util.HashMap;
import java.util.Map;

public class InstanceCarFactory {
	private Map<String, Car> cars = null;
	public InstanceCarFactory() {
		cars=new HashMap<>();
		cars.put("audi", new Car("Audi", 300000));
		cars.put("bmw", new Car("BMW", 300000));
	}
	
	public Car getCar(String brand) {
		return cars.get(brand);
	}
}
