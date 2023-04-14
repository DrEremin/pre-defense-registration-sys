package ru.dreremin.predefense.registration.sys.controllers.registration;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.dreremin.predefense.registration.sys.dto.request.RegistrationRequestDto;
import ru.dreremin.predefense.registration.sys.dto.response.StatusResponseDto;
import ru.dreremin.predefense.registration.sys.services.registration.CreateRegistrationService;

//@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
@Slf4j
@RequiredArgsConstructor
@RestController
public class CreateRegistrationController {

	private final CreateRegistrationService service;
	
	@PutMapping(value = "/student/registrations/create", 
			consumes = "application/json")
	public StatusResponseDto studentRegistration(
			@Valid @RequestBody RegistrationRequestDto dto) {
		
		service.createStudentRegistration(dto.getComissionId());
		log.info("CreateRegistrationController."
				+ "createStudentRegistration() is success");
		return new StatusResponseDto(200, "Ok");
	}
	
	@PutMapping(value = "/teacher/registrations/create", consumes = "application/json")
	public StatusResponseDto teacherRegistration(
			@Valid @RequestBody RegistrationRequestDto dto) {
		service.createTeacherRegistration(dto.getComissionId());
		log.info("CreateRegistrationController."
				+ "createTeacherRegistration() success");
		return new StatusResponseDto(200, "Ok");
	}
}
