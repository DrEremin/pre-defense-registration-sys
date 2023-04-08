package ru.dreremin.predefense.registration.sys.controllers.exception;

import javax.naming.AuthenticationException;
import javax.persistence.EntityNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.mail.MailException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedCredentialsNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;

import ru.dreremin.predefense.registration.sys.dto.response.StatusDto;
import ru.dreremin.predefense.registration.sys.exceptions
		 .EntitiesMismatchException;
import ru.dreremin.predefense.registration.sys.exceptions.ExpiredCommissionException;
import ru.dreremin.predefense.registration.sys.exceptions.FailedAuthenticationException;
import ru.dreremin.predefense.registration.sys.exceptions.InvalidJwtTokenException;
import ru.dreremin.predefense.registration.sys.exceptions
		 .NegativeTimePeriodException;
import ru.dreremin.predefense.registration.sys.exceptions.OverLimitException;
import ru.dreremin.predefense.registration.sys.exceptions
		 .UniquenessViolationException;

@ControllerAdvice
public class ExceptionController {

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
	
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<StatusDto> handleBadCredentialsException(
			BadCredentialsException e) {
		return new ResponseEntity<>(new StatusDto(403, 
						"Incorrect credentials"), 
				HttpStatus.FORBIDDEN);
	}
	
	@ExceptionHandler(ExpiredCommissionException.class)
	public ResponseEntity<StatusDto> handleExpiredComissionException(
			ExpiredCommissionException e) {
		return new ResponseEntity<>(new StatusDto(409, e.getMessage()), 
				HttpStatus.CONFLICT);
	}
	
	/**
	 * JWT Exceptions
	 */
	
	@ExceptionHandler(InvalidJwtTokenException.class)
	public ResponseEntity<StatusDto> handleInvalidJwtTokenException(
			InvalidJwtTokenException e) {
		return new ResponseEntity<>(new StatusDto(400, e.getMessage()), 
									HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(TokenExpiredException.class)
	public ResponseEntity<StatusDto> handleTokenExpiredException(
			TokenExpiredException e) {
		return new ResponseEntity<>(new StatusDto(403,
				"This token was expired"), 
				HttpStatus.FORBIDDEN);
	}
	
	@ExceptionHandler(JWTDecodeException.class)
	public ResponseEntity<StatusDto> handleJWTDecodeException(
			JWTDecodeException e) {
		return new ResponseEntity<>(new StatusDto(403, 
				"This token is decoded incorrectly"), 
				HttpStatus.FORBIDDEN);
	}
	
	@ExceptionHandler(SignatureVerificationException.class)
	public ResponseEntity<StatusDto> handleSignatureVerificationException(
			SignatureVerificationException e) {
		return new ResponseEntity<>(new StatusDto(403, 
				"Token signature verification failed"), 
				HttpStatus.FORBIDDEN);
	}
	
	@ExceptionHandler(AlgorithmMismatchException.class)
	public ResponseEntity<StatusDto> handleAlgorithmMismatchException(
			AlgorithmMismatchException e) {
		return new ResponseEntity<>(new StatusDto(403, 
				"The token is encoded by another algorithm"), 
				HttpStatus.FORBIDDEN);
	}
	
	@ExceptionHandler(InvalidClaimException.class)
	public ResponseEntity<StatusDto> handleInvalidClaimException(
			InvalidClaimException e) {
		return new ResponseEntity<>(new StatusDto(403, "Invalid claim"), 
				HttpStatus.FORBIDDEN);
	}
	/**
	 * JWT Exceptions
	 */
	
	@ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
	public ResponseEntity<StatusDto> 
			handleAuthenticationCredentialsNotFoundException(
					AuthenticationCredentialsNotFoundException e) {
		return new ResponseEntity<>(new StatusDto(403, e.getMessage()), 
				HttpStatus.FORBIDDEN);
	}
	
	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<StatusDto> handleAuthenticationException(
			AuthenticationException e) {
		return new ResponseEntity<>(new StatusDto(403, "User is not authorized"), 
				HttpStatus.FORBIDDEN);
	}
	
}