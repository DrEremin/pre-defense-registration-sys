package ru.dreremin.predefense.registration.sys.controllers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import ru.dreremin.predefense.registration.sys.dto.responsedto.StatusDto;
import ru.dreremin.predefense.registration.sys.exceptions
		 .NegativeTimePeriodException;
import ru.dreremin.predefense.registration.sys.exceptions
		 .UniquenessViolationException;

@ControllerAdvice
public class ExceptionsController {

	@ExceptionHandler(UniquenessViolationException.class)
	public ResponseEntity<StatusDto> handleUniquenessViolationException(
			UniquenessViolationException e) {
		return new ResponseEntity<>(new StatusDto(409, e.getMessage()), 
									HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<StatusDto> handleMethodArgumentNotValidException(
			MethodArgumentNotValidException e) {
		return new ResponseEntity<>(new StatusDto(
											400, 
											"Invalid format request body field"),
									HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<StatusDto> handleHttpMessageNotReadableException(
			HttpMessageNotReadableException e) {
		return new ResponseEntity<>(new StatusDto(400, 
												  "Failed to read request body"), 
									HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(NegativeTimePeriodException.class)
	public ResponseEntity<StatusDto> handleNegativeTimePeriodException(
			NegativeTimePeriodException e) {
		return new ResponseEntity<>(new StatusDto(400, e.getMessage()), 
									HttpStatus.BAD_REQUEST);
	}
}