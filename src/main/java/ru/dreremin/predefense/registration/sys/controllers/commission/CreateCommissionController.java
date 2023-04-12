package ru.dreremin.predefense.registration.sys.controllers.commission;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.dreremin.predefense.registration.sys.dto.request.CommissionDto;
import ru.dreremin.predefense.registration.sys.dto.response.StatusDto;
import ru.dreremin.predefense.registration.sys.services.commission.CreateCommissionService;

@Slf4j
@RequiredArgsConstructor
@RestController
public class CreateCommissionController {
	
	private final CreateCommissionService service;
	
	@PutMapping(value = "/admin/commissions/create", 
			consumes = "application/json")
	public StatusDto createComission(@Valid @RequestBody CommissionDto dto) {
		dto.periodValidation();
		service.createComission(dto);
		log.info("CreateComissionController.createComission() success");
		return new StatusDto(200, "Ok");
	}
 
}
