package ru.dreremin.predefense.registration.sys.exceptions;

public class ExpiredCommissionException extends RuntimeException {

	public ExpiredCommissionException(String message) {
		super(message);
	}
	
	@Override
	public String toString() {
		return "OverdueComissionException: " + super.getMessage();
	}
}
