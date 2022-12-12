package ru.dreremin.predefense.registration.sys.exceptions;

public class NegativeTimePeriodException extends RuntimeException {

	public NegativeTimePeriodException(String message) {
		super(message);
	}
	
	@Override
	public String toString() {
		return "NegativeTimePeriodException: " + super.getMessage();
	}
}
