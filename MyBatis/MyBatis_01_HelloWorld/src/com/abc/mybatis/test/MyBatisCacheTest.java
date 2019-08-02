package com.abc.mybatis.test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.MappedByteBuffer;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.Test;

import com.abc.mybatis.bean.Employee;
import com.abc.mybatis.dao.EmployeeMapper;

public class MyBatisCacheTest {
	public SqlSessionFactory getSqlSessionFactory() throws IOException {
		String resource = "mybatis-config.xml";
		InputStream inputStream = Resources.getResourceAsStream(resource);
		return new SqlSessionFactoryBuilder().build(inputStream);
	}
	
	@Test
	public void testFirstLevelCache() throws IOException {
		SqlSessionFactory sqlSessionFactory=getSqlSessionFactory();
		SqlSession openSession=sqlSessionFactory.openSession(true);
		try {
			EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);
			Employee emp = mapper.getEmpById(1);
			System.out.println(emp);
			
			
			Employee emp2 = mapper.getEmpById(1);
			System.out.println(emp2);
		} finally {
			openSession.close();
		}
	}
	
	@Test
	public void testSecondLevelCache() throws IOException {
		SqlSessionFactory sqlSessionFactory=getSqlSessionFactory();
		SqlSession openSession=sqlSessionFactory.openSession(true);
		SqlSession openSession2=sqlSessionFactory.openSession(true);
		try {
			EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);
			EmployeeMapper mapper2 = openSession2.getMapper(EmployeeMapper.class);
			Employee emp = mapper.getEmpById(1);
			System.out.println(emp);
			openSession.close();
			
			Employee emp2 = mapper2.getEmpById(1);
			System.out.println(emp2);
		} finally {
			
			openSession2.close();
		}
	}	
}
