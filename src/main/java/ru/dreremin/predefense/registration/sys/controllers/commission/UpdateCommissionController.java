package ru.dreremin.predefense.registration.sys.controllers.commission;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.dreremin.predefense.registration.sys.dto.request.CommissionRequestDto;
import ru.dreremin.predefense.registration.sys.dto.response.StatusResponseDto;
import ru.dreremin.predefense.registration.sys.services.commission.UpdateCommissionService;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UpdateCommissionController {
	
	private final UpdateCommissionService updateCommissionService;
	
	@PutMapping(value = "/admin/commissions/update", 
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<StatusResponseDto> updateCommission(
			@RequestBody CommissionRequestDto dto) {
		
		updateCommissionService.updateCommission(dto);
		log.info("UpdateCommissionController.updateCommission() is success");
		return ResponseEntity.ok(new StatusResponseDto(200, "Ok"));
	}
}
