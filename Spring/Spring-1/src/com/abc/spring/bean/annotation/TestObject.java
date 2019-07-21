package com.abc.spring.bean.annotation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.abc.spring.bean.annotation.repository.UserRepositoryImpl;

@Component
public class TestObject {
	
	@Autowired
	private UserRepositoryImpl ur;
	
	public void go() {
		ur.save();
	}
}
