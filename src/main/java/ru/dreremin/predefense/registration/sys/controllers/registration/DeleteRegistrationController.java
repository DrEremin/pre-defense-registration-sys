package ru.dreremin.predefense.registration.sys.controllers.registration;

import javax.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.dreremin.predefense.registration.sys.dto.request.RegistrationRequestDto;
import ru.dreremin.predefense.registration.sys.dto.response.StatusResponseDto;
import ru.dreremin.predefense.registration.sys.services.registration.DeleteRegistrationService;

//@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
@Slf4j
@RequiredArgsConstructor
@RestController
public class DeleteRegistrationController {
	
	private final DeleteRegistrationService service;
	
	@DeleteMapping(value = "/student/registrations/delete")
	public StatusResponseDto deleteStudentRegistration() {
		
		service.deleteStudentRegistration();
		log.info("DeleteRegistrationController."
				+ "deleteStudentRegistration() is success");
		return new StatusResponseDto(200, "Ok");
	}
	
	@DeleteMapping(value = "/teacher/registrations/delete", 
			consumes = "application/json")
	public StatusResponseDto deleteTeacherRegistration(
			@Valid @RequestBody RegistrationRequestDto dto) {
		
		service.deleteTeacherRegistration(dto.getComissionId());
		log.info("DeleteRegistrationController."
				+ "deleteTeacherRegistration() is success");
		return new StatusResponseDto(200, "Ok");
	}
}
