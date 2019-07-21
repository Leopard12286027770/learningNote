package com.abc.jdbc.tx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("store")
public class StoreImpl implements StoreDao{
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public int findPriceByDeptId(String deptName) {
		String sql = "select price from dept where dept_name = ?";
		return jdbcTemplate.queryForObject(sql, Integer.class, deptName);
	}

	@Override
	public void decreaseNumber(String deptName) {
		String sql2 = "select number from dept  where dept_name = ?";
		int num = jdbcTemplate.queryForObject(sql2, Integer.class,deptName);
		if(num<=0) {
			throw new NumberNotEnoughException("not enough num in dept");
		}
		
		String sql = "update dept set number = number-1  where dept_name = ?";
		jdbcTemplate.update(sql, deptName);
		
	}

	@Override
	public void updateMoney(String userName, int price) {
		String sql2 = "select money from employee  where last_name = ?";
		int num = jdbcTemplate.queryForObject(sql2, Integer.class,userName);
		if(num<price) {
			throw new MoneyNotEnoughException("not enough money");
		}		
		
		String sql = "update employee set money = money-?  where last_name = ?";
		jdbcTemplate.update(sql, price,userName);
		
	}


}
