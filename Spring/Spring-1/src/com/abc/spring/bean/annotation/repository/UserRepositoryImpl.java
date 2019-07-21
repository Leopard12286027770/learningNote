package com.abc.spring.bean.annotation.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.abc.spring.bean.annotation.TestObject;

@Repository("userRepo")
public class UserRepositoryImpl implements UserRepository{
	@Override
	public void save() {
		System.out.println("UserRepository implemented  repository");
		
	}
	
}
