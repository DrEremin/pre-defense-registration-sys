package ru.dreremin.predefense.registration.sys.controllers.commission;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.dreremin.predefense.registration.sys.dto.request.RegistrationRequestDto;
import ru.dreremin.predefense.registration.sys.dto.response.StatusResponseDto;
import ru.dreremin.predefense.registration.sys.services.commission.DeleteCommissionService;

@Slf4j
@RequiredArgsConstructor
@RestController
public class DeleteCommissionController {
	
	private final DeleteCommissionService service;
	
	@DeleteMapping(value = "/admin/commissions/delete", 
			consumes = "application/json")
	public StatusResponseDto createComission(@Valid @RequestBody RegistrationRequestDto dto) {
		service.deleteComission(dto.getComissionId());
		log.info("DeleteComissionController.deleteComission() is success");
		return new StatusResponseDto(200, "Ok");
	}
}
