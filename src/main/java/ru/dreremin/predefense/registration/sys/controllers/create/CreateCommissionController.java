package ru.dreremin.predefense.registration.sys.controllers.create;

import javax.validation.Valid;

import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.dreremin.predefense.registration.sys.dto.requestdto.CommissionDto;
import ru.dreremin.predefense.registration.sys.dto.responsedto.StatusDto;
import ru.dreremin.predefense.registration.sys.exceptions
		 .NegativeTimePeriodException;
import ru.dreremin.predefense.registration.sys.services.comissions
		 .CreateCommissionService;

@Slf4j
@RequiredArgsConstructor
@RestController
public class CreateCommissionController {
	
	private final CreateCommissionService service;
	
	@PutMapping(value = "/comissions/create", consumes = "application/json")
	public StatusDto createComission(@Valid @RequestBody CommissionDto dto) 
			throws NegativeTimePeriodException, 
			MethodArgumentNotValidException,
			HttpMessageNotReadableException {
		dto.periodValidation();
		service.createComission(dto);
		log.info("CreateComissionController.createComission() success");
		return new StatusDto(200, "Ok");
	}
 
}
