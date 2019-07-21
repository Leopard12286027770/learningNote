package com.abc.jdbc.tx;

import java.util.List;

public interface Cashier {
	public void checkout(String userName, List<String> depts);
}
