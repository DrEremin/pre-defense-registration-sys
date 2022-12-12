package ru.dreremin.predefense.registration.sys.exceptions;

public class ExpiredComissionException extends RuntimeException {

	public ExpiredComissionException(String message) {
		super(message);
	}
	
	@Override
	public String toString() {
		return "OverdueComissionException: " + super.getMessage();
	}
}
