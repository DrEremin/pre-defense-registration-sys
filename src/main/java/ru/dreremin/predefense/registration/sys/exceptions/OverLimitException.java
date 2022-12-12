package ru.dreremin.predefense.registration.sys.exceptions;

public class OverLimitException extends RuntimeException {

	public OverLimitException(String message) {
		super(message);
	}
	
	@Override
	public String toString() {
		return "OverLimitException: " + super.getMessage();
	}
}
