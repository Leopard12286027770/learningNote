package com.abc.jdbc.tx;

public class MoneyNotEnoughException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MoneyNotEnoughException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MoneyNotEnoughException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public MoneyNotEnoughException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public MoneyNotEnoughException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public MoneyNotEnoughException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
	
}
