package ru.dreremin.predefense.registration.sys.controllers.admin;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.dreremin.predefense.registration.sys.dto.request.LoginRequestDto;
import ru.dreremin.predefense.registration.sys.dto.response.StatusResponseDto;
import ru.dreremin.predefense.registration.sys.services.admin.DeleteAdministratorService;

@Slf4j
@RequiredArgsConstructor
@RestController
public class DeleteAdministratorController {
	
	private final DeleteAdministratorService deleteAdministratorService;
	
	@DeleteMapping("/admin/users/delete/admin")
	public ResponseEntity<StatusResponseDto> deleteAdmin(
			@Valid @RequestBody LoginRequestDto dto) {
		
		deleteAdministratorService.deleteAdmin(dto.getLogin());
		log.info("DeleteAdministratorController.deleteAdmin() is success");
		return ResponseEntity.ok(new StatusResponseDto(200, "Ok"));
	}
}
