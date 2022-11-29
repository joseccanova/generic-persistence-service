package org.nanotek.ormservice.service;

public class ServiceRuntimeException extends Exception {

	private static final long serialVersionUID = 7137714208019493804L;

	public ServiceRuntimeException() {
		super();
	}

	public ServiceRuntimeException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ServiceRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public ServiceRuntimeException(String message) {
		super(message);
	}

	public ServiceRuntimeException(Throwable cause) {
		super(cause);
	}

	
}
