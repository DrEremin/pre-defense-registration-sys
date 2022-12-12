package ru.dreremin.predefense.registration.sys.exceptions;

public class FailedAuthenticationException extends RuntimeException {

	public FailedAuthenticationException(String message) {
		super(message);
	}
	
	@Override
	public String toString() {
		return "FailedAuthenticationException: " + super.getMessage();
	}
}
