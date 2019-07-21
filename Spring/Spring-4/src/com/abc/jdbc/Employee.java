package com.abc.jdbc;

public class Employee {
	private Integer id;
	private String lastName;
	private String email;
	private Department detp;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Department getDetp() {
		return detp;
	}
	public void setDetp(Department detp) {
		this.detp = detp;
	}
	@Override
	public String toString() {
		return "Employee [id=" + id + ", lastName=" + lastName + ", email=" + email + ", detp=" + detp + "]";
	}
	
}
