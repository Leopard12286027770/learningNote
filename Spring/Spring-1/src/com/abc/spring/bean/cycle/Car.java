package com.abc.spring.bean.cycle;

public class Car {
	public Car() {
		System.out.println("Constructor Called");
	}
	
	private String brand;

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		System.out.println("setBrand() called");
		this.brand = brand;
	}

	@Override
	public String toString() {
		return "Car [brand=" + brand + "]";
	}
	
	public void init() {
		System.out.println("init method called");
	}
	
	public void destroy() {
		System.out.println("destroy method called");
	}
	
}
