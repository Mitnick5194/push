package com.ajie.push.exception;

public class PushException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public PushException() {
		super();
	}

	public PushException(String msg) {
		super(msg);
	}

	public PushException(Throwable e) {
		super(e);
	}

	public PushException(String msg, Throwable e) {
		super(msg, e);
	}

}
