package ru.dreremin.predefense.registration.sys.exceptions;

public class NotReadableRequestParameterException extends RuntimeException {

	public NotReadableRequestParameterException(String message) {
		super(message);
	}
	
	@Override
	public String toString() {
		return "NotReadableRequestParameterException: " + super.getMessage();
	}
}
