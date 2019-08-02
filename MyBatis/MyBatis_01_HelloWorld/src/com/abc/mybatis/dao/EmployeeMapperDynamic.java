package com.abc.mybatis.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.abc.mybatis.bean.Employee;

public interface EmployeeMapperDynamic {
	
	public List<Employee> getEmpsByConditionIf(Employee employee);
	
	public List<Employee> getEmpsByConditionTrim(Employee employee);
	
	public List<Employee> getEmpsByConditionChoose(Employee employee);
	
	public void updateEmp(Employee employee);
	
	public List<Employee> getEmpsByConditionForeach(List<Integer> ls);
	
	public void addEmps(@Param("emps")List<Employee> emps);
}
