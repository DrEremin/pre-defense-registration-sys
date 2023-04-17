package ru.dreremin.predefense.registration.sys.controllers.commission;

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
	
	@DeleteMapping(value = "/admin/commissions/delete/by-id/{id}")
	public ResponseEntity<StatusResponseDto> createComission(
			@PathVariable(value = "id") int commissionId) {
		service.deleteComission(commissionId);
		log.info("DeleteComissionController.deleteComission() is success");
		return ResponseEntity.ok(new StatusResponseDto(200, "Ok"));
	}
}
