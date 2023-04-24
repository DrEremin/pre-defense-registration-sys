package ru.dreremin.predefense.registration.sys.controllers.admin;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import ru.dreremin.predefense.registration.sys.dto.request
		 .AdministratorRequestDto;
import ru.dreremin.predefense.registration.sys.dto.response.StatusResponseDto;
import ru.dreremin.predefense.registration.sys.services.admin
		 .UpdateAdministratorService;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UpdateAdministratorController {
	
	private final UpdateAdministratorService updateAdministratorService;
	
	@PatchMapping("/admin/{id}")
	public ResponseEntity<StatusResponseDto> updateAdministrator(
			@PathVariable(value = "id")
			@Min(1) 
			@Max(Long.MAX_VALUE) 
			long id,
			@RequestBody
			AdministratorRequestDto dto) {
		
		updateAdministratorService.updateAdministrator(dto, id);
		
		log.info(
				"UpdateAdministratorController.updateAdministrator() success");
		return ResponseEntity.ok(new StatusResponseDto(200, "Ok"));
	}
}
