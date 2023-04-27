package ru.dreremin.predefense.registration.sys.controllers.commission;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import ru.dreremin.predefense.registration.sys.dto.response
		 .CommissionResponseDto;
import ru.dreremin.predefense.registration.sys.dto.response
		 .WrapperForPageResponseDto;
import ru.dreremin.predefense.registration.sys.models.Commission;
import ru.dreremin.predefense.registration.sys.services.commission
		 .ReadCommissionService;

//@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
@RequiredArgsConstructor
@Slf4j
@RestController
public class ReadCommissionController {
	
	private final ReadCommissionService service;
	
	@GetMapping("/commission/list")
	public ResponseEntity<WrapperForPageResponseDto
	<Commission, CommissionResponseDto>> getCommissionsList(
			@RequestParam(value = "page", defaultValue = "0") 
			@Valid @Min(0)@Max(Integer.MAX_VALUE) int page, 
			@RequestParam(value = "amountOfItemsOnPage", defaultValue = "10") 
			@Valid @Min(0) @Max(Integer.MAX_VALUE) int size,
			@RequestParam(
					value = "startDateTime", 
					required = false, 
					defaultValue = "") String startDateTime,
			@RequestParam(
					value = "endDateTime", 
					required = false, 
					defaultValue = "") String endDateTime,
			@RequestParam(
					value = "my", 
					required = false, 
					defaultValue = "") String my) {
		
			WrapperForPageResponseDto<Commission, CommissionResponseDto> 
					result = service.getCommissionsList(
							PageRequest.of(
									page, 
									size, 
									Sort.by(Sort.Order.asc("startDateTime"))), 
							startDateTime, 
							endDateTime,
							my);
			log.info("ReadComissionController.getCommissionsList()"
					+ "is success");
			return ResponseEntity.ok(result);
	}
	
	@GetMapping(value = "/commission/{id}")
	public ResponseEntity<CommissionResponseDto> getComissionById(
					@PathVariable(value = "id")
					@Min(0) 
					@Max(Integer.MAX_VALUE) 
					int id) {
		CommissionResponseDto commission = service
				.getCommissionById(id, true, true, true);
		log.info("ReadComissionController.getCommission() is success");
		return ResponseEntity.ok(commission);
	}
}
