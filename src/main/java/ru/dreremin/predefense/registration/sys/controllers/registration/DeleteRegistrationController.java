package ru.dreremin.predefense.registration.sys.controllers.registration;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import ru.dreremin.predefense.registration.sys.dto.response.StatusResponseDto;
import ru.dreremin.predefense.registration.sys.services.registration
		 .DeleteRegistrationService;

//@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
@Slf4j
@RequiredArgsConstructor
@RestController
public class DeleteRegistrationController {
	
	private final DeleteRegistrationService service;
	
	@DeleteMapping("/student/registrations/delete")
	public ResponseEntity<StatusResponseDto> deleteStudentRegistration() {
		
		service.deleteStudentRegistration();
		log.info("DeleteRegistrationController."
				+ "deleteStudentRegistration() is success");
		return ResponseEntity.ok(new StatusResponseDto(200, "Ok"));
	}
	
	@DeleteMapping("/teacher/registrations/delete/by-commission-id/{id}")
	public ResponseEntity<StatusResponseDto> deleteTeacherRegistration(
			@PathVariable(value = "id") 
			@Min(1) 
			@Max(Integer.MAX_VALUE) 
			int commissionId) {
		
		service.deleteTeacherRegistration(commissionId);
		log.info("DeleteRegistrationController."
				+ "deleteTeacherRegistration() is success");
		return ResponseEntity.ok(new StatusResponseDto(200, "Ok"));
	}
}
