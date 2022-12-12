package ru.dreremin.predefense.registration.sys.exceptions;

public class EntitiesMismatchException extends RuntimeException {

	public EntitiesMismatchException(String message) {
		super(message);
	}
	
	@Override
	public String toString() {
		return "EntitiesMismatchException: " + super.getMessage();
	}
}
