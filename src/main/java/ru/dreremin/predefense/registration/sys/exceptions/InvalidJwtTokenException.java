package ru.dreremin.predefense.registration.sys.exceptions;

public class InvalidJwtTokenException extends RuntimeException {

	public InvalidJwtTokenException(String message) {
		super(message);
	}
	
	@Override
	public String toString() {
		return "InvalidJwtTokenException: " + super.getMessage();
	}
}
