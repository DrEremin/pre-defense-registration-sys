package ru.dreremin.predefense.registration.sys.controllers.registration;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import ru.dreremin.predefense.registration.sys.dto.response.StatusResponseDto;
import ru.dreremin.predefense.registration.sys.services.registration
		 .CreateRegistrationService;

//@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
@Slf4j
@RequiredArgsConstructor
@RestController
public class CreateRegistrationController {

	private final CreateRegistrationService service;
	
	@PutMapping("/student/registrations/create/by-commission-id/{id}")
	public ResponseEntity<StatusResponseDto> studentRegistration(
			@PathVariable(value = "id") int commissionId) {
		
		service.createStudentRegistration(commissionId);
		log.info("CreateRegistrationController."
				+ "createStudentRegistration() is success");
		return ResponseEntity.ok(new StatusResponseDto(200, "Ok"));
	}
	
	@PutMapping("/teacher/registrations/create/by-commission-id/{id}")
	public ResponseEntity<StatusResponseDto> teacherRegistration(
			@PathVariable(value = "id") int commissionId) {
		service.createTeacherRegistration(commissionId);
		log.info("CreateRegistrationController."
				+ "createTeacherRegistration() success");
		return ResponseEntity.ok(new StatusResponseDto(200, "Ok"));
	}
}
