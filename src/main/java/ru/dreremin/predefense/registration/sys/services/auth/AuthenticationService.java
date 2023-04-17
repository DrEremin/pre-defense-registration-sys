package ru.dreremin.predefense.registration.sys.services.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication
		  .UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import ru.dreremin.predefense.registration.sys.dto.request
		 .AuthenticationRequestDto;
import ru.dreremin.predefense.registration.sys.security.JwtTokenProvider;

@RequiredArgsConstructor
@Service
public class AuthenticationService {
	
	private final AuthenticationManager authenticationManager;
	private final JwtTokenProvider jwtTokenProvider;
	
	public String getToken(AuthenticationRequestDto dto) {

		UsernamePasswordAuthenticationToken authToken = 
				new UsernamePasswordAuthenticationToken(
						dto.getLogin(), 
						dto.getPassword());
		
		authenticationManager.authenticate(authToken);
		return jwtTokenProvider.generateToken(dto.getLogin());
	}
}
