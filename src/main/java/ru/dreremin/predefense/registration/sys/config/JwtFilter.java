package ru.dreremin.predefense.registration.sys.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.exceptions.JWTVerificationException;

import lombok.RequiredArgsConstructor;
import ru.dreremin.predefense.registration.sys.exceptions.InvalidJwtTokenException;
import ru.dreremin.predefense.registration.sys.security.JwtTokenProvider;
import ru.dreremin.predefense.registration.sys.services.authentication.ActorDetailsService;


@RequiredArgsConstructor
@Component
public class JwtFilter extends OncePerRequestFilter {

	private final JwtTokenProvider jwtTokenProvider;
	private final ActorDetailsService actorDetailsService;
	
	
	
	@Override
	protected void doFilterInternal(
			HttpServletRequest request, 
			HttpServletResponse response, 
			FilterChain filterChain) throws ServletException, IOException {
		
String authHeader = request.getHeader("Authorization");
		
		if (authHeader != null 
				&& !authHeader.isBlank() 
				&& authHeader.startsWith("Bearer ")) {
			
			String jwt = authHeader.substring(7);
			
			if (jwt.isBlank()) {
				throw new InvalidJwtTokenException(
						"Invalid JWT token in Bearer Header");
			} else {
				try {
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
					throw new JWTVerificationException("Invalid JWT token");
				}
			}
		}
		filterChain.doFilter(request, response);
	}
}
