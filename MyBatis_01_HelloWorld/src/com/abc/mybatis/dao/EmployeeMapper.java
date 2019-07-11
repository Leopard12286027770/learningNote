package com.abc.mybatis.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.MapKey;

import com.abc.mybatis.bean.Employee;

public interface EmployeeMapper {
	
	@MapKey("id")
	public Map<Integer, Employee> getEmpByLastNameLikeReturnMap(String lastName);
	
	public Map<String, Object> getEmpByIdReturnMap(Integer id);
	
	public List<Employee> getEmpsByLastNameLike(String lastName);
	
	public Employee getEmpById(Integer id);
	
	public void addEmp(Employee employee);
	
	public void updateEmp(Employee employee);
	
	public void deleteEmpById(Integer id);
}
