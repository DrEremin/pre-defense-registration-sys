package ru.dreremin.predefense.registration.sys.controllers.create;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.dreremin.predefense.registration.sys.dto.requestdto.RegistrationDto;
import ru.dreremin.predefense.registration.sys.dto.responsedto.StatusDto;
import ru.dreremin.predefense.registration.sys.services.registrations
		 .CreateRegistrationService;

@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/registrations/create")
public class CreateRegistrationController {

	private final CreateRegistrationService service;
	
	@PutMapping(value = "/student")
	public StatusDto studentRegistration(
			@RequestBody RegistrationDto dto) {
		
		service.createStudentRegistration(dto.getComissionId());
		log.info("CreateRegistrationController."
				+ "createStudentRegistration() is success");
		return new StatusDto(200, "Ok");
	}
	
	@PutMapping(value = "/teacher", consumes = "application/json")
	public StatusDto teacherRegistration(
			@Valid @RequestBody RegistrationDto dto) {
		service.createTeacherRegistration(dto.getComissionId());
		log.info("CreateRegistrationController."
				+ "createTeacherRegistration() success");
		return new StatusDto(200, "Ok");
	}
}
