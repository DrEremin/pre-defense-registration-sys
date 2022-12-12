package ru.dreremin.predefense.registration.sys.controllers.exceptions;

import javax.persistence.EntityNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.mail.MailException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.auth0.jwt.exceptions.JWTVerificationException;

import ru.dreremin.predefense.registration.sys.dto.responsedto.StatusDto;
import ru.dreremin.predefense.registration.sys.exceptions
		 .EntitiesMismatchException;
import ru.dreremin.predefense.registration.sys.exceptions.ExpiredComissionException;
import ru.dreremin.predefense.registration.sys.exceptions.FailedAuthenticationException;
import ru.dreremin.predefense.registration.sys.exceptions.InvalidJwtTokenException;
import ru.dreremin.predefense.registration.sys.exceptions
		 .NegativeTimePeriodException;
import ru.dreremin.predefense.registration.sys.exceptions.OverLimitException;
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
						400, "Invalid format request body field"),
				HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<StatusDto> handleHttpMessageNotReadableException(
			HttpMessageNotReadableException e) {
		return new ResponseEntity<>(new StatusDto(
						400, "Failed to read request body"), 
				HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(NegativeTimePeriodException.class)
	public ResponseEntity<StatusDto> handleNegativeTimePeriodException(
			NegativeTimePeriodException e) {
		return new ResponseEntity<>(new StatusDto(400, e.getMessage()), 
									HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<StatusDto> handleEntityNotFoundException(
			EntityNotFoundException e) {
		return new ResponseEntity<>(new StatusDto(409, e.getMessage()), 
									HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(OverLimitException.class)
	public ResponseEntity<StatusDto> handleEntityAbsenceException(
			OverLimitException e) {
		return new ResponseEntity<>(new StatusDto(409, e.getMessage()), 
									HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(EntitiesMismatchException.class)
	public ResponseEntity<StatusDto> handleEntityAbsenceException(
			EntitiesMismatchException e) {
		return new ResponseEntity<>(new StatusDto(409, e.getMessage()), 
									HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(FailedAuthenticationException.class)
	public ResponseEntity<StatusDto> handleEntityAbsenceException(
			FailedAuthenticationException e) {
		return new ResponseEntity<>(new StatusDto(409, e.getMessage()), 
									HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(MailException.class)
	public ResponseEntity<StatusDto> handleMailException(
			MailException e) {
		return new ResponseEntity<>(new StatusDto(500, "Error sending email"), 
									HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(UsernameNotFoundException.class)
	public ResponseEntity<StatusDto> handleUsernameNotFoundException(
			UsernameNotFoundException e) {
		return new ResponseEntity<>(new StatusDto(409, e.getMessage()), 
									HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(InvalidJwtTokenException.class)
	public ResponseEntity<StatusDto> handleInvalidJwtTokenException(
			InvalidJwtTokenException e) {
		return new ResponseEntity<>(new StatusDto(409, e.getMessage()), 
									HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(JWTVerificationException.class)
	public ResponseEntity<StatusDto> handleInvalidJwtTokenException(
			JWTVerificationException e) {
		return new ResponseEntity<>(new StatusDto(403, e.getMessage()), 
				HttpStatus.FORBIDDEN);
	}
	
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<StatusDto> handleBadCredentialsException(
			BadCredentialsException e) {
		return new ResponseEntity<>(new StatusDto(403, 
						"Incorrect credentials"), 
				HttpStatus.FORBIDDEN);
	}
	
	@ExceptionHandler(ExpiredComissionException.class)
	public ResponseEntity<StatusDto> handleExpiredComissionException(
			ExpiredComissionException e) {
		return new ResponseEntity<>(new StatusDto(409, e.getMessage()), 
				HttpStatus.CONFLICT);
	}
}