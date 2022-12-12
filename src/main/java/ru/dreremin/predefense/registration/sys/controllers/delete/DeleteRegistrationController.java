package ru.dreremin.predefense.registration.sys.controllers.delete;

import javax.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.dreremin.predefense.registration.sys.dto.requestdto.RegistrationDto;
import ru.dreremin.predefense.registration.sys.dto.responsedto.StatusDto;
import ru.dreremin.predefense.registration.sys.services.registrations
		 .DeleteRegistrationService;

//@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/registrations/delete")
public class DeleteRegistrationController {
	
	private final DeleteRegistrationService service;
	
	@DeleteMapping(value = "/student")
	public StatusDto deleteStudentRegistration() {
		
		service.deleteStudentRegistration();
		log.info("DeleteRegistrationController."
				+ "deleteStudentRegistration() is success");
		return new StatusDto(200, "Ok");
	}
	
	@DeleteMapping(value = "/teacher", consumes = "application/json")
	public StatusDto deleteTeacherRegistration(
			@Valid @RequestBody RegistrationDto dto) {
		
		service.deleteTeacherRegistration(dto.getComissionId());
		log.info("DeleteRegistrationController."
				+ "deleteTeacherRegistration() is success");
		return new StatusDto(200, "Ok");
	}
}
