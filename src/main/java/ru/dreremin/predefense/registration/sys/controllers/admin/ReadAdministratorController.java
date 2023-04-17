package ru.dreremin.predefense.registration.sys.controllers.admin;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import ru.dreremin.predefense.registration.sys.dto.response
		 .AdministratorResponseDto;
import ru.dreremin.predefense.registration.sys.dto.response
		 .WrapperForPageResponseDto;
import ru.dreremin.predefense.registration.sys.models.Actor;
import ru.dreremin.predefense.registration.sys.services.admin
		 .ReadAdministratorService;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ReadAdministratorController {
	
	private final ReadAdministratorService readAdministratorService;
	
	@GetMapping("/admin/users/read/admins/all")
	public ResponseEntity<WrapperForPageResponseDto<
			Actor, AdministratorResponseDto>> getAllStudents(
					@RequestParam(value = "page", defaultValue = "0") 
					@Min(0)
					@Max(Integer.MAX_VALUE)
					int page, 
					@RequestParam(value = "size", defaultValue = "10") 
					@Min(0)
					@Max(Integer.MAX_VALUE)
					int size) {
		
		WrapperForPageResponseDto<Actor, AdministratorResponseDto> 
				administrators = readAdministratorService.getAllAdministrators(
						PageRequest.of(
								page, 
								size, 
								Sort.by(Sort.Order.asc("login"))));
		
		log.info("ReadAdministratorController.getAllAdministrators() success");
		return ResponseEntity.ok(administrators);
	}
}
