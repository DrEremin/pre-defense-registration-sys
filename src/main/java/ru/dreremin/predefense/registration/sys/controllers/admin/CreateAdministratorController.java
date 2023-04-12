package ru.dreremin.predefense.registration.sys.controllers.admin;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.dreremin.predefense.registration.sys.dto.request.AdminDto;
import ru.dreremin.predefense.registration.sys.dto.response.JwtTokenDto;
import ru.dreremin.predefense.registration.sys.services.admin.CreateAdministratorService;


@Slf4j
@RequiredArgsConstructor
@RestController
public class CreateAdministratorController {
	
	private final CreateAdministratorService adminService;
	
	@PutMapping(value = "/admin/users/create/admin", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<JwtTokenDto> createAdmin(
			@Valid @RequestBody AdminDto adminDto) {
		String jwtToken = adminService.createAdmin(adminDto);
		log.info("CreateAdministratorController.createStudent() success");
		return ResponseEntity.ok(new JwtTokenDto(jwtToken));
	}
}
