package ru.dreremin.predefense.registration.sys.exceptions;

import com.auth0.jwt.exceptions.JWTVerificationException;

public class InvalidJwtTokenException extends JWTVerificationException {

	public InvalidJwtTokenException(String message) {
		super(message);
	}
	
	@Override
	public String toString() {
		return "InvalidJwtTokenException: " + super.getMessage();
	}
}
