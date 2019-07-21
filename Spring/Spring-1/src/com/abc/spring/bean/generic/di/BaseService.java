package com.abc.spring.bean.generic.di;

import org.springframework.beans.factory.annotation.Autowired;

public class BaseService<T> {
	@Autowired
	protected BaseRepository<T> repository;
	
	public void add() {
		System.out.println("addd     in service");
		System.out.println(repository);
	}
}
