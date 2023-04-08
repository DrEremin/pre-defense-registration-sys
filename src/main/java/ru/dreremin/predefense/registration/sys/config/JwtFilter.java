package ru.dreremin.predefense.registration.sys.config;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.dreremin.predefense.registration.sys.controllers.exception.ExceptionController;
import ru.dreremin.predefense.registration.sys.dto.response.StatusDto;
import ru.dreremin.predefense.registration.sys.exceptions.InvalidJwtTokenException;
import ru.dreremin.predefense.registration.sys.security.JwtTokenProvider;
import ru.dreremin.predefense.registration.sys.services.auth.ActorDetailsService;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtFilter extends OncePerRequestFilter {

	private final JwtTokenProvider jwtTokenProvider;
	
	private final ActorDetailsService actorDetailsService;
	
	private final ExceptionController exceptionController;
	
	private HttpServletResponse errResponse;
	
	@Autowired
	private ObjectMapper mapper = new ObjectMapper();
	
	@Override
	protected void doFilterInternal(
			HttpServletRequest request, 
			HttpServletResponse response, 
			FilterChain filterChain) throws ServletException, IOException {
		
		String authHeader = request.getHeader("Authorization");
		errResponse = response;
		if (authHeader != null) {
			try {
				if (authHeader.isBlank() || !authHeader.startsWith("Bearer_")) {
					throw new InvalidJwtTokenException(
							"Invalid  Bearer header of JWT token");
				} 
				
				String jwt = authHeader.substring(7);
				
				if (jwt.isBlank()) {
					throw new InvalidJwtTokenException(
							"JWT token body is empty");
				}	
				
				String login = jwtTokenProvider
						.validateTokenAndRetrieveClaim(jwt);
				UserDetails userDetails = actorDetailsService
						.loadUserByUsername(login);
				UsernamePasswordAuthenticationToken authToken = 
						new UsernamePasswordAuthenticationToken(
								userDetails, 
								userDetails.getPassword(), 
								userDetails.getAuthorities());
				
				if (SecurityContextHolder.getContext().getAuthentication() 
						== null) {
					SecurityContextHolder.getContext()
							.setAuthentication(authToken);
				}
			} catch (JWTVerificationException e) {
				responseWithException(e);
				return;
			}
		}
		filterChain.doFilter(request, response);
	}
	
	protected void responseWithException(JWTVerificationException e) 
			throws IOException {
		
		ResponseEntity<StatusDto> dto = null;
		
		if (e instanceof TokenExpiredException) {
			dto = exceptionController
					.handleTokenExpiredException((TokenExpiredException) e);
		} else if (e instanceof JWTDecodeException) {
			dto = exceptionController
					.handleJWTDecodeException((JWTDecodeException) e);
		} else if (e instanceof SignatureVerificationException) {
			dto = exceptionController
					.handleSignatureVerificationException(
							(SignatureVerificationException) e);
		} else if (e instanceof AlgorithmMismatchException) {
			dto = exceptionController
					.handleAlgorithmMismatchException(
							(AlgorithmMismatchException) e);
		} else if (e instanceof InvalidClaimException) {
			dto = exceptionController
					.handleInvalidClaimException((InvalidClaimException) e);
		} else if (e instanceof InvalidJwtTokenException) {
			dto = exceptionController
					.handleInvalidJwtTokenException(
							(InvalidJwtTokenException) e);
		}
		errResponse.setStatus(dto.getStatusCodeValue());
		errResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
		
		PrintWriter out = errResponse.getWriter();
		
		out.print(mapper.writeValueAsString(dto.getBody()));
		out.flush();
	}
}
