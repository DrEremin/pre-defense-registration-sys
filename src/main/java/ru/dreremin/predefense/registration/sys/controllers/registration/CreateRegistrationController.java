package ru.dreremin.predefense.registration.sys.controllers.registration;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
	
	@PostMapping("/commission/{id}/registration")
	public ResponseEntity<StatusResponseDto> createRegistration(
			@PathVariable(value = "id") 
			@Min(1) 
			@Max(Integer.MAX_VALUE) 
			int commissionId) {
		service.createRegistration(commissionId);
		log.info("CreateRegistrationController."
				+ "createRegistration() success");
		return ResponseEntity.ok(new StatusResponseDto(200, "Ok"));
	}
}
