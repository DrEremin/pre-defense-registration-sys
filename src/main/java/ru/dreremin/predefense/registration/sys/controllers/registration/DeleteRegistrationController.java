package ru.dreremin.predefense.registration.sys.controllers.registration;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	
	@DeleteMapping("/registration/commission/{id}")
	public ResponseEntity<StatusResponseDto> createRegistration(
			@RequestParam(value = "id", required = false, defaultValue = "0") 
			@Min(0) 
			@Max(Integer.MAX_VALUE) 
			int commissionId) {
		service.deleteRegistration(commissionId);
		log.info("DeleteRegistrationController."
				+ "deleteRegistration() success");
		return ResponseEntity.ok(new StatusResponseDto(200, "Ok"));
	}
}
