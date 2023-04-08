package ru.dreremin.predefense.registration.sys.controllers.admin;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import ru.dreremin.predefense.registration.sys.dto.requestdto.LoginDto;
import ru.dreremin.predefense.registration.sys.dto.responsedto.StatusDto;
import ru.dreremin.predefense.registration.sys.services.admins
		 .DeleteAdministratorService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/users/delete")
public class DeleteAdministratorController {
	
	private final DeleteAdministratorService deleteAdministratorService;
	
	@DeleteMapping("/admin")
	public ResponseEntity<StatusDto> deleteAdmin(
			@Valid @RequestBody LoginDto dto) {
		
		deleteAdministratorService.deleteAdmin(dto.getLogin());
		log.info("DeleteAdministratorController.deleteAdmin() is success");
		return ResponseEntity.ok(new StatusDto(200, "Ok"));
	}
}
