package ru.dreremin.predefense.registration.sys.services.auth;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.dreremin.predefense.registration.sys.dto.request.AuthenticationRequestDto;
import ru.dreremin.predefense.registration.sys.exceptions
		 .FailedAuthenticationException;
import ru.dreremin.predefense.registration.sys.models.Administrator;
import ru.dreremin.predefense.registration.sys.models.Actor;
import ru.dreremin.predefense.registration.sys.models.Student;
import ru.dreremin.predefense.registration.sys.models.Teacher;
import ru.dreremin.predefense.registration.sys.repositories
		 .AdministratorRepository;
import ru.dreremin.predefense.registration.sys.repositories
		 .ActorRepository;
import ru.dreremin.predefense.registration.sys.repositories.StudentRepository;
import ru.dreremin.predefense.registration.sys.repositories.TeacherRepository;
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
