package ru.dreremin.predefense.registration.sys.controllers.exception;

import java.time.format.DateTimeParseException;
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
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;

import lombok.extern.slf4j.Slf4j;
import ru.dreremin.predefense.registration.sys.dto.response.StatusResponseDto;
import ru.dreremin.predefense.registration.sys.exceptions
		 .EntitiesMismatchException;
import ru.dreremin.predefense.registration.sys.exceptions
		 .ExpiredCommissionException;
import ru.dreremin.predefense.registration.sys.exceptions
		 .FailedAuthenticationException;
import ru.dreremin.predefense.registration.sys.exceptions
		 .InvalidJwtTokenException;
import ru.dreremin.predefense.registration.sys.exceptions
		 .NegativeTimePeriodException;
import ru.dreremin.predefense.registration.sys.exceptions
		 .NotReadableRequestParameterException;
import ru.dreremin.predefense.registration.sys.exceptions.OverLimitException;
import ru.dreremin.predefense.registration.sys.exceptions
		 .UniquenessViolationException;

@Slf4j
@ControllerAdvice
public class ExceptionController {

	@ExceptionHandler(UniquenessViolationException.class)
	public ResponseEntity<StatusResponseDto> 
			handleUniquenessViolationException(
					UniquenessViolationException e) {
		return new ResponseEntity<>(
				new StatusResponseDto(409, e.getMessage()), 
				HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<StatusResponseDto> 
			handleMethodArgumentNotValidException(
					MethodArgumentNotValidException e) {
		return new ResponseEntity<>(
				new StatusResponseDto(
						400, 
						"Invalid format request body field"),
				HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<StatusResponseDto> 
			handleHttpMessageNotReadableException(
						HttpMessageNotReadableException e) {
		return new ResponseEntity<>(
				new StatusResponseDto(
				400, "Failed to read request body"), 
				HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(NegativeTimePeriodException.class)
	public ResponseEntity<StatusResponseDto> handleNegativeTimePeriodException(
			NegativeTimePeriodException e) {
		return new ResponseEntity<>(
				new StatusResponseDto(400, e.getMessage()), 
				HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<StatusResponseDto> handleEntityNotFoundException(
			EntityNotFoundException e) {
		StackTraceElement[] elements = e.getStackTrace();
		for (StackTraceElement element : elements) {
			log.debug(element.toString());
		}
		return new ResponseEntity<>(
				new StatusResponseDto(409, e.getMessage()), 
				HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(OverLimitException.class)
	public ResponseEntity<StatusResponseDto> handleEntityAbsenceException(
			OverLimitException e) {
		return new ResponseEntity<>(
				new StatusResponseDto(409, e.getMessage()), 
				HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(EntitiesMismatchException.class)
	public ResponseEntity<StatusResponseDto> handleEntityAbsenceException(
			EntitiesMismatchException e) {
		return new ResponseEntity<>(
				new StatusResponseDto(409, e.getMessage()), 
				HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(FailedAuthenticationException.class)
	public ResponseEntity<StatusResponseDto> handleEntityAbsenceException(
			FailedAuthenticationException e) {
		return new ResponseEntity<>(
				new StatusResponseDto(409, e.getMessage()), 
				HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(MailException.class)
	public ResponseEntity<StatusResponseDto> handleMailException(
			MailException e) {
		return new ResponseEntity<>(
				new StatusResponseDto(500, "Error sending email"), 
				HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(UsernameNotFoundException.class)
	public ResponseEntity<StatusResponseDto> handleUsernameNotFoundException(
			UsernameNotFoundException e) {
		return new ResponseEntity<>(
				new StatusResponseDto(409, e.getMessage()), 
				HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<StatusResponseDto> handleBadCredentialsException(
			BadCredentialsException e) {
		return new ResponseEntity<>(
				new StatusResponseDto(403, "Incorrect credentials"), 
				HttpStatus.FORBIDDEN);
	}
	
	@ExceptionHandler(ExpiredCommissionException.class)
	public ResponseEntity<StatusResponseDto> handleExpiredComissionException(
			ExpiredCommissionException e) {
		return new ResponseEntity<>(
				new StatusResponseDto(409, e.getMessage()), 
				HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(DateTimeParseException.class)
	public ResponseEntity<StatusResponseDto> handleDateTimeParseException(
			DateTimeParseException e) {
		return new ResponseEntity<>(
				new StatusResponseDto(400, "Incorrect zoned date time format"), 
				HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(NotReadableRequestParameterException.class)
	public ResponseEntity<StatusResponseDto> 
			handleNotReadableRequestParameterException(
					NotReadableRequestParameterException e) {
		return new ResponseEntity<>(
				new StatusResponseDto(400, e.getMessage()), 
				HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * JWT Exceptions
	 */
	
	@ExceptionHandler(InvalidJwtTokenException.class)
	public ResponseEntity<StatusResponseDto> handleInvalidJwtTokenException(
			InvalidJwtTokenException e) {
		return new ResponseEntity<>(
				new StatusResponseDto(400, e.getMessage()), 
				HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(TokenExpiredException.class)
	public ResponseEntity<StatusResponseDto> handleTokenExpiredException(
			TokenExpiredException e) {
		return new ResponseEntity<>(
				new StatusResponseDto(
						403,
						"This token was expired"), 
				HttpStatus.FORBIDDEN);
	}
	
	@ExceptionHandler(JWTDecodeException.class)
	public ResponseEntity<StatusResponseDto> handleJWTDecodeException(
			JWTDecodeException e) {
		return new ResponseEntity<>(
				new StatusResponseDto(
						403, 
						"This token is decoded incorrectly"), 
				HttpStatus.FORBIDDEN);
	}
	
	@ExceptionHandler(SignatureVerificationException.class)
	public ResponseEntity<StatusResponseDto> 
			handleSignatureVerificationException(
					SignatureVerificationException e) {
		return new ResponseEntity<>(
				new StatusResponseDto(
						403, 
						"Token signature verification failed"), 
				HttpStatus.FORBIDDEN);
	}
	
	@ExceptionHandler(AlgorithmMismatchException.class)
	public ResponseEntity<StatusResponseDto> handleAlgorithmMismatchException(
			AlgorithmMismatchException e) {
		return new ResponseEntity<>(
				new StatusResponseDto(
						403, 
						"The token is encoded by another algorithm"), 
				HttpStatus.FORBIDDEN);
	}
	
	@ExceptionHandler(InvalidClaimException.class)
	public ResponseEntity<StatusResponseDto> handleInvalidClaimException(
			InvalidClaimException e) {
		return new ResponseEntity<>(
				new StatusResponseDto(403, "Invalid claim"), 
				HttpStatus.FORBIDDEN);
	}
	
	/*
	@ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
	public ResponseEntity<StatusResponseDto> 
			handleAuthenticationCredentialsNotFoundException(
					AuthenticationCredentialsNotFoundException e) {
		return new ResponseEntity<>(
				new StatusResponseDto(403, e.getMessage()), 
				HttpStatus.FORBIDDEN);
	}
	
	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<StatusResponseDto> handleAuthenticationException(
			AuthenticationException e) {
		return new ResponseEntity<>(
				new StatusResponseDto(403, "User is not authorized"), 
				HttpStatus.FORBIDDEN);
	}
	
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<StatusResponseDto> handleAccessDeniedException(
			AccessDeniedException e) {
		return new ResponseEntity<>(
				new StatusResponseDto(403, e.getMessage()), 
				HttpStatus.FORBIDDEN);
	}
	*/
}