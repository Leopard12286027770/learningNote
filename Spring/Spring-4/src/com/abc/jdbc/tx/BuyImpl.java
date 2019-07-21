package com.abc.jdbc.tx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service("buy")
public class BuyImpl implements Buy{
	
	@Autowired
	private StoreDao storeDao;
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void buy(String userName, String deptName) {
		int price = storeDao.findPriceByDeptId(deptName);
		storeDao.decreaseNumber(deptName);
		storeDao.updateMoney(userName, price);
	}

}
