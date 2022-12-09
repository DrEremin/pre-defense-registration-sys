package ru.dreremin.predefense.registration.sys.controllers.auth;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.dreremin.predefense.registration.sys.dto.requestdto.AuthenticationDto;
import ru.dreremin.predefense.registration.sys.dto.responsedto.JwtTokenDto;
import ru.dreremin.predefense.registration.sys.services.authentication.ActorDetailsService;
import ru.dreremin.predefense.registration.sys.services.authentication.AuthenticationService;

@RequiredArgsConstructor
@RestController
@RequestMapping()
public class AuthenticationController {
	
	private final AuthenticationService authenticationService;
	
	@PostMapping("/login")
	public ResponseEntity<JwtTokenDto> login(
			@Valid @RequestBody AuthenticationDto dto) {
		
		String jwtToken = authenticationService.login(dto);
		
		return ResponseEntity.ok(new JwtTokenDto(jwtToken));
	}
}
