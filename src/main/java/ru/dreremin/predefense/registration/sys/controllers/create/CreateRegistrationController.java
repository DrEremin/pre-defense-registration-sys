package ru.dreremin.predefense.registration.sys.controllers.create;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.dreremin.predefense.registration.sys.dto.requestdto.RegistrationDto;
import ru.dreremin.predefense.registration.sys.dto.responsedto.StatusDto;
import ru.dreremin.predefense.registration.sys.exceptions
		 .EntitiesMismatchException;
import ru.dreremin.predefense.registration.sys.exceptions
		 .FailedAuthenticationException;
import ru.dreremin.predefense.registration.sys.exceptions.OverLimitException;
import ru.dreremin.predefense.registration.sys.exceptions
		 .UniquenessViolationException;
import ru.dreremin.predefense.registration.sys.services.registrations
		 .CreateRegistrationService;

@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/registration")
public class CreateRegistrationController {

	private final CreateRegistrationService service;
	
	@PutMapping(value = "/student", consumes = "application/json")
	public StatusDto studentRegistration(
			@Valid @RequestBody RegistrationDto dto) 
					throws EntityNotFoundException, 
					OverLimitException, 
					EntitiesMismatchException,
					UniquenessViolationException,
					FailedAuthenticationException, 
					MethodArgumentNotValidException {
		service.createStudentRegistration(dto);
		log.info("CreateRegistrationController."
				+ "createStudentRegistration() success");
		return new StatusDto(200, "Ok");
	}
	
	@PutMapping(value = "/teacher", consumes = "application/json")
	public StatusDto teacherRegistration(
			@Valid @RequestBody RegistrationDto dto) 
					throws FailedAuthenticationException, 
					EntityNotFoundException, 
					UniquenessViolationException, 
					MethodArgumentNotValidException {
		service.createTeacherRegistration(dto);
		log.info("CreateRegistrationController."
				+ "createTeacherRegistration() success");
		return new StatusDto(200, "Ok");
	}
}
