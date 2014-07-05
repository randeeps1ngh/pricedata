package com.sample.pricedata.exception;

public class PriceDataCacheException extends Exception {

	private static final long serialVersionUID = 6264890635767516825L;

	private String message;

	public PriceDataCacheException(String message) {
		this.message = message;
	}

	public String getMessage() {
		return this.message;
	}

}
