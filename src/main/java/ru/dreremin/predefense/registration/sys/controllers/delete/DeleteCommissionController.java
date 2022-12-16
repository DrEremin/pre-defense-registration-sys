package ru.dreremin.predefense.registration.sys.controllers.delete;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.dreremin.predefense.registration.sys.dto.requestdto.RegistrationDto;
import ru.dreremin.predefense.registration.sys.dto.responsedto.StatusDto;
import ru.dreremin.predefense.registration.sys.services.comissions.DeleteCommissionService;

@Slf4j
@RequiredArgsConstructor
@RestController
public class DeleteCommissionController {
	
	private final DeleteCommissionService service;
	
	@DeleteMapping(value = "/comissions/delete", consumes = "application/json")
	public StatusDto createComission(@Valid @RequestBody RegistrationDto dto) {
		service.deleteComission(dto.getComissionId());
		log.info("DeleteComissionController.deleteComission() is success");
		return new StatusDto(200, "Ok");
	}
}
