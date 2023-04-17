package ru.dreremin.predefense.registration.sys.controllers.commission;

import javax.validation.Valid;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import ru.dreremin.predefense.registration.sys.dto.request
		 .CommissionRequestDto;
import ru.dreremin.predefense.registration.sys.dto.response.StatusResponseDto;
import ru.dreremin.predefense.registration.sys.services.commission
		 .CreateCommissionService;

@Slf4j
@RequiredArgsConstructor
@RestController
public class CreateCommissionController {
	
	private final CreateCommissionService service;
	
	@PutMapping(value = "/admin/commissions/create", 
			consumes = "application/json")
	public StatusResponseDto createComission(
			@Valid @RequestBody CommissionRequestDto dto) {
		dto.periodValidation();
		service.createComission(dto);
		log.info("CreateComissionController.createComission() success");
		return new StatusResponseDto(200, "Ok");
	}
 
}
