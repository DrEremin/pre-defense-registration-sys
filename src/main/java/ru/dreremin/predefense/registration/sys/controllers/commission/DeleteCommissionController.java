package ru.dreremin.predefense.registration.sys.controllers.commission;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import ru.dreremin.predefense.registration.sys.dto.response.StatusResponseDto;
import ru.dreremin.predefense.registration.sys.services.commission
		 .DeleteCommissionService;

@Slf4j
@RequiredArgsConstructor
@RestController
public class DeleteCommissionController {
	
	private final DeleteCommissionService service;
	
	@DeleteMapping(value = "/commission/{id}")
	public ResponseEntity<StatusResponseDto> deleteComission(
			@PathVariable(value = "id") 
			@Min(1) 
			@Max(Integer.MAX_VALUE) 
			int commissionId) {
		service.deleteComission(commissionId);
		log.info("DeleteComissionController.deleteComission() is success");
		return ResponseEntity.ok(new StatusResponseDto(200, "Ok"));
	}
}
