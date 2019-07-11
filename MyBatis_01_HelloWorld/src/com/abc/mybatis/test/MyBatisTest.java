package com.abc.mybatis.test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.Test;

import com.abc.mybatis.bean.Department;
import com.abc.mybatis.bean.Employee;
import com.abc.mybatis.dao.DepartmentMapper;
import com.abc.mybatis.dao.EmployeeMapper;
import com.abc.mybatis.dao.EmployeeMapperAnnotation;
import com.abc.mybatis.dao.EmployeeMapperDynamic;
import com.abc.mybatis.dao.EmployeeMapperPlus;
//import com.sun.xml.internal.ws.developer.UsesJAXBContext;

class MyBatisTest {
	public SqlSessionFactory getSqlSessionFactory() throws IOException {
		String resource = "mybatis-config.xml";
		InputStream inputStream = Resources.getResourceAsStream(resource);
		return new SqlSessionFactoryBuilder().build(inputStream);
	}

	@Test
	void test() throws IOException {

		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();

		SqlSession openSession = sqlSessionFactory.openSession();

		try {
			// (unique identifier , Object parameter)
			// identifier: namespace.id
			Employee employee = openSession.selectOne("com.abc.mybatis.EmployeeMapper.selectEmp", 1);
			System.out.println(employee);
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			openSession.close();
		}

	}

	@Test
	public void test01() throws IOException {
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		SqlSession openSession = sqlSessionFactory.openSession();
		try {
			EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);
			Employee employee = mapper.getEmpById(1);
			System.out.println(employee);			
		} finally {
			openSession.close();
		}
	}
	
	@Test
	public void test02() throws IOException {
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		SqlSession openSession = sqlSessionFactory.openSession();
		try {
			EmployeeMapperAnnotation mapper = openSession.getMapper(EmployeeMapperAnnotation.class);
			Employee employee = mapper.getEmpById(1);
			System.out.println(employee);			
		} finally {
			openSession.close();
		}
	}
	
	@Test
	public void test03() throws IOException {
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		SqlSession openSession = sqlSessionFactory.openSession(true);
		try {
			EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);
			Employee employee = new Employee(500, "chris", "6cccccc@abc.com", "0");
			mapper.addEmp(employee);
			System.out.println(employee);
//			mapper.updateEmp(new Employee(2, "harry", "77777@UsesJAXBContext.com", "1"));
//			mapper.deleteEmpById(2);
//			openSession.commit();		
		} finally {
			openSession.close();
		}
	}
	
	@Test
	public void test04() throws IOException {
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		SqlSession openSession = sqlSessionFactory.openSession(true);
		try {
			EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);
			List<Employee> ls = mapper.getEmpsByLastNameLike("%r%");
			for(Employee e:ls) {
				System.out.println(e);
			}
		} finally {
			openSession.close();
		}
	}	
	
	@Test
	public void test05() throws IOException {
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		SqlSession openSession = sqlSessionFactory.openSession(true);
		try {
			EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);
			Map<String, Object> ls = mapper.getEmpByIdReturnMap(1);
				System.out.println(ls);
		} finally {
			openSession.close();
		}
	}	
	
	@Test
	public void test06() throws IOException {
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		SqlSession openSession = sqlSessionFactory.openSession(true);
		try {
			EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);
			Map<Integer, Employee> ls = mapper.getEmpByLastNameLikeReturnMap("%r%");
				System.out.println(ls);
		} finally {
			openSession.close();
		}
	}
	
	@Test
	public void test07() throws IOException {
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		SqlSession openSession = sqlSessionFactory.openSession(true);
		try {
			EmployeeMapperPlus mapper = openSession.getMapper(EmployeeMapperPlus.class);
			Employee ls = mapper.getEmpAndDept(1);
			System.out.println(ls);
			System.out.println(ls.getDept());
		} finally {
			openSession.close();
		}
	}	
	
	@Test
	public void test08() throws IOException {
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		SqlSession openSession = sqlSessionFactory.openSession(true);
		try {
			EmployeeMapperPlus mapper = openSession.getMapper(EmployeeMapperPlus.class);
			Employee ls = mapper.getEmpByIdStep(3);
			System.out.println(ls);
			System.out.println(ls.getDept());
		} finally {
			openSession.close();
		}
	}
	
	@Test
	public void test09() throws IOException {
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		SqlSession openSession = sqlSessionFactory.openSession(true);
		try {
			DepartmentMapper mapper = openSession.getMapper(DepartmentMapper.class);
//			Department ls = mapper.getDeptByIdPlus(1);
			Department ls = mapper.getDeptByIdStep(1);
			System.out.println(ls);
			System.out.println(ls.getEmps());
		} finally {
			openSession.close();
		}
	}

	@Test
	public void test10() throws IOException {
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		SqlSession openSession = sqlSessionFactory.openSession(true);
		try {
			EmployeeMapperDynamic mapper = openSession.getMapper(EmployeeMapperDynamic.class);
			Employee employee=new Employee(null,"H","7777555@123456.com","0",new Department(1));
//			List<Employee> ls = mapper.getEmpsByConditionIf(employee);
//			List<Employee> ls= mapper.getEmpsByConditionTrim(employee);
//			mapper.updateEmp(employee);
//			employee.setLastName(null);
//			List<Employee> ll = new LinkedList<>();
//			ll.add(employee);
//			ll.add(employee);
//			mapper.addEmps(ll);
			List<Employee> ls= mapper.getEmpsByConditionChoose(employee);
//			List<Employee> ls = mapper.getEmpsByConditionForeach(Arrays.asList(1,2,3,4,5,6));
			System.out.println(ls);
		} finally {
			openSession.close();
		}
	}	

}
