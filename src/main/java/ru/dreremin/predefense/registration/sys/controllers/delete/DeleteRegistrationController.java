package ru.dreremin.predefense.registration.sys.controllers.delete;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.dreremin.predefense.registration.sys.dto.requestdto.impl
		 .AuthenticationDto;
import ru.dreremin.predefense.registration.sys.dto.requestdto.impl
		 .RegistrationDto;
import ru.dreremin.predefense.registration.sys.dto.responsedto.StatusDto;
import ru.dreremin.predefense.registration.sys.exceptions
		 .FailedAuthenticationException;
import ru.dreremin.predefense.registration.sys.services.registrations
		 .DeleteRegistrationService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/registration-delete")
public class DeleteRegistrationController {
	
	private final DeleteRegistrationService service;
	
	@DeleteMapping(value = "/student", consumes = "application/json")
	public StatusDto deleteStudentRegistration(
			@Valid @RequestBody AuthenticationDto dto) 
					throws EntityNotFoundException, 
					FailedAuthenticationException, 
					MethodArgumentNotValidException,
					HttpMessageNotReadableException {
		
		service.deleteStudentRegistration(dto);
		log.info("DeleteRegistrationController."
				+ "deleteStudentRegistration() success");
		return new StatusDto(200, "Ok");
	}
	
	@DeleteMapping(value = "/teacher", consumes = "application/json")
	public StatusDto deleteTeacherRegistration(
			@Valid @RequestBody RegistrationDto dto) 
					throws EntityNotFoundException, 
					FailedAuthenticationException, 
					MethodArgumentNotValidException,
					HttpMessageNotReadableException {
		
		service.deleteTeacherRegistration(dto);
		log.info("DeleteRegistrationController."
				+ "deleteTeacherRegistration() success");
		return new StatusDto(200, "Ok");
	}
}
