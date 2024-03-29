package ru.dreremin.predefense.registration.sys.controllers.commission;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import ru.dreremin.predefense.registration.sys.dto.request
		 .CommissionRequestDto;
import ru.dreremin.predefense.registration.sys.dto.response.StatusResponseDto;
import ru.dreremin.predefense.registration.sys.services.commission
		 .UpdateCommissionService;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UpdateCommissionController {
	
	private final UpdateCommissionService updateCommissionService;
	
	@PatchMapping(
			value = "commission/{id}", 
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<StatusResponseDto> updateCommission(
			@PathVariable(value = "id")
			@Min(1) 
			@Max(Integer.MAX_VALUE) 
			int id,
			@RequestBody CommissionRequestDto dto) {
		
		updateCommissionService.updateCommission(id, dto);
		log.info("UpdateCommissionController.updateCommission() is success");
		return ResponseEntity.ok(new StatusResponseDto(200, "Ok"));
	}
}
