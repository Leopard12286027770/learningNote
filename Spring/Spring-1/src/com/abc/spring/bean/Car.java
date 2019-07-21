package com.abc.spring.bean;

public class Car {
	 private String brand;
	 private String speed;
	public Car() {
		super();
	}
	public Car(String brand, String speed) {
		super();
		this.brand = brand;
		this.speed = speed;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getSpeed() {
		return speed;
	}
	public void setSpeed(String speed) {
		this.speed = speed;
	}
	@Override
	public String toString() {
		return "Car [brand=" + brand + ", speed=" + speed + "]";
	}
 
	
 
}
