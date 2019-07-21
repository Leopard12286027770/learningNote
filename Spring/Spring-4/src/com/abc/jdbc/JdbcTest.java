package com.abc.jdbc;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

class JdbcTest {
	private ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
//	@Autowired
//	private JdbcTemplate jdbcTemplate;
	private JdbcTemplate jdbcTemplate= (JdbcTemplate) ctx.getBean("jdbcTemplate");
	
	@Test
	void testDataSource() throws SQLException {
		DataSource dataSource = (DataSource) ctx.getBean("dataSource");
		System.out.println(dataSource.getConnection());
	}

	@Test
	void testUpdate() throws SQLException {
		String sql = "UPDATE employee SET last_name=? WHERE id = ?";
		int res = jdbcTemplate.update(sql, "Peter",1);
		System.out.println(res);
	}
	
	@Test
	public void testQueryForObject() {
		String sql = "select id, last_name lastName, email from employee where id=?";
		RowMapper<Employee> rowMapper= new BeanPropertyRowMapper<>(Employee.class);
		Employee employee = jdbcTemplate.queryForObject(sql, rowMapper, 1);
		System.out.println(employee);
	}

	@Test
	public void testQueryForList() {
		String sql = "select id, last_name lastName, email from employee where last_name like ?";
		RowMapper<Employee> rowMapper= new BeanPropertyRowMapper<>(Employee.class);
		List<Employee> employees = jdbcTemplate.query(sql, rowMapper, "%e%");
		System.out.println(employees);
	}
	
	@Test
	public void testAutowired() {
		EmployeeDao employeeDao = ctx.getBean(EmployeeDao.class);
		System.out.println(employeeDao.getEmpById(1));
	}
}
