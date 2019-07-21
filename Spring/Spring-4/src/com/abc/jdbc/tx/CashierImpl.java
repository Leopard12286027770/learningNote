package com.abc.jdbc.tx;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service("cashier")
public class CashierImpl implements Cashier{
	@Autowired
	private Buy buy;

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void checkout(String userName, List<String> depts) {
		for(String dept:depts) {
			buy.buy(userName, dept);
		}
		
	}
}
