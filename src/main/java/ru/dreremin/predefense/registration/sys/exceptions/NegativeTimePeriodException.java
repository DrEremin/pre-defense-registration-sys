package ru.dreremin.predefense.registration.sys.exceptions;

public class NegativeTimePeriodException extends Exception {

	public NegativeTimePeriodException(String message) {
		super(message);
	}
	
	@Override
	public String toString() {
		return "NegativeTimePeriodException: " + super.getMessage();
	}
}
