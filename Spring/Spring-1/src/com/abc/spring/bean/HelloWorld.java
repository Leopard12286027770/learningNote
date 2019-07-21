package com.abc.spring.bean;

public class HelloWorld {
	private String name;

	public HelloWorld() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void hello() {
		System.out.println("hello  "+name);
	}
}
