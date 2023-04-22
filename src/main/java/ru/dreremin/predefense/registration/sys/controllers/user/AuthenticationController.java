package ru.dreremin.predefense.registration.sys.controllers.user;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.dreremin.predefense.registration.sys.dto.request
		 .AuthenticationRequestDto;
import ru.dreremin.predefense.registration.sys.dto.response
		 .JwtTokenResponseDto;
import ru.dreremin.predefense.registration.sys.services.auth
		 .AuthenticationService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class AuthenticationController {
	
	private final AuthenticationService authenticationService;
	
	@PostMapping(
			value = "/login", 
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<JwtTokenResponseDto> login(
			@Valid @RequestBody AuthenticationRequestDto dto) {
		
		String jwtToken = authenticationService.getToken(dto);
		
		return ResponseEntity.ok(new JwtTokenResponseDto(jwtToken));
	}
}
