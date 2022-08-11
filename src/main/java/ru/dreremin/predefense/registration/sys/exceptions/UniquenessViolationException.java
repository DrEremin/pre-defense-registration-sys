package ru.dreremin.predefense.registration.sys.exceptions;

public class UniquenessViolationException extends Exception {

	public UniquenessViolationException(String message) {
		super(message);
	}
	
	@Override
	public String toString() {
		return "UniquenessViolationException: " + super.getMessage();
	}
	
	
}
