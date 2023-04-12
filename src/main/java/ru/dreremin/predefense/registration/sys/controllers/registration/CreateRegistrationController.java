package ru.dreremin.predefense.registration.sys.controllers.registration;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.dreremin.predefense.registration.sys.dto.request.RegistrationDto;
import ru.dreremin.predefense.registration.sys.dto.response.StatusDto;
import ru.dreremin.predefense.registration.sys.services.registration.CreateRegistrationService;

//@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
@Slf4j
@RequiredArgsConstructor
@RestController
public class CreateRegistrationController {

	private final CreateRegistrationService service;
	
	@PutMapping(value = "/student/registrations/create", 
			consumes = "application/json")
	public StatusDto studentRegistration(
			@Valid @RequestBody RegistrationDto dto) {
		
		service.createStudentRegistration(dto.getComissionId());
		log.info("CreateRegistrationController."
				+ "createStudentRegistration() is success");
		return new StatusDto(200, "Ok");
	}
	
	@PutMapping(value = "/teacher/registrations/create", consumes = "application/json")
	public StatusDto teacherRegistration(
			@Valid @RequestBody RegistrationDto dto) {
		service.createTeacherRegistration(dto.getComissionId());
		log.info("CreateRegistrationController."
				+ "createTeacherRegistration() success");
		return new StatusDto(200, "Ok");
	}
}
