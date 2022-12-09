package ru.dreremin.predefense.registration.sys.security;

import java.time.ZonedDateTime;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

@Component
public class JwtTokenProvider {
	
	@Value("${jwt.token.secret}")
	private String secret;
	
	@Value("${jwt.token.expiration.minutes}")
	private long minutes;
	
	public String generateToken(String login) {
		
		Date expirationDate = Date.from(
				ZonedDateTime.now().plusMinutes(minutes).toInstant());
		return JWT.create()
				.withSubject("Actor details")
				.withClaim("login", login)
				.withIssuedAt(new Date())
				.withIssuer("jwt-security")
				.withExpiresAt(expirationDate)
				.sign(Algorithm.HMAC256(secret));
	}
	
	public String validateTokenAndRetrieveClaim(String token) 
			throws JWTVerificationException {
		JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
				.withSubject("Actor details")
				.withIssuer("jwt-security")
				.build();
		
		DecodedJWT jwt = verifier.verify(token);
		return jwt.getClaim("login").asString();
	}
}
