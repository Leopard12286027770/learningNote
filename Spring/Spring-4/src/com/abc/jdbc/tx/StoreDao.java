package com.abc.jdbc.tx;

public interface StoreDao {
	public int findPriceByDeptId(String deptName);
	
	public void decreaseNumber(String deptName);
	
	public void updateMoney(String userName, int price);
}
