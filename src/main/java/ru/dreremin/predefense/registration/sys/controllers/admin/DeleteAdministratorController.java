package ru.dreremin.predefense.registration.sys.controllers.admin;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import ru.dreremin.predefense.registration.sys.dto.response.StatusResponseDto;
import ru.dreremin.predefense.registration.sys.services.admin
		 .DeleteAdministratorService;

@Slf4j
@RequiredArgsConstructor
@RestController
public class DeleteAdministratorController {
	
	private final DeleteAdministratorService deleteAdministratorService;
	
	@DeleteMapping("/users/admin/delete/{id}")
	public ResponseEntity<StatusResponseDto> deleteAdmin(
			@PathVariable(value = "id") @Min(0) @Max(Long.MAX_VALUE) long id) {
		
		deleteAdministratorService.deleteAdmin(id);
		log.info("DeleteAdministratorController.deleteAdmin() is success");
		return ResponseEntity.ok(new StatusResponseDto(200, "Ok"));
	}
}
