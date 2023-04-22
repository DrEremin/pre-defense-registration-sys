package ru.dreremin.predefense.registration.sys.controllers.admin;

import javax.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import ru.dreremin.predefense.registration.sys.dto.request.AdministratorRequestDto;
import ru.dreremin.predefense.registration.sys.dto.response.StatusResponseDto;
import ru.dreremin.predefense.registration.sys.services.admin
		 .CreateAdministratorService;


@Slf4j
@RequiredArgsConstructor
@RestController
public class CreateAdministratorController {
	
	private final CreateAdministratorService adminService;
	
	@PostMapping(
			value = "/users/admin/create", 
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<StatusResponseDto> createAdmin(
			@Valid @RequestBody AdministratorRequestDto administratorRequestDto) {
		adminService.createAdmin(administratorRequestDto);
		log.info("CreateAdministratorController.createStudent() success");
		return ResponseEntity.ok(new StatusResponseDto(200, "Ok"));
	}
}
