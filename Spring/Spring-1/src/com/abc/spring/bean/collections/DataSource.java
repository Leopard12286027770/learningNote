package com.abc.spring.bean.collections;

import java.util.Properties;

public class DataSource {
	private Properties properties;

	public DataSource() {
		super();
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	@Override
	public String toString() {
		return "DataSource [properties=" + properties + "]";
	}
	
	
}
