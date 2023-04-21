package ru.dreremin.predefense.registration.sys.controllers.commission;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import ru.dreremin.predefense.registration.sys.dto.request
		 .TimePeriodRequestDto;
import ru.dreremin.predefense.registration.sys.dto.response
		 .CommissionResponseDto;
import ru.dreremin.predefense.registration.sys.dto.response
		 .CurrentCommissionResponseDto;
import ru.dreremin.predefense.registration.sys.dto.response
		 .WrapperForPageResponseDto;
import ru.dreremin.predefense.registration.sys.models.Actor;
import ru.dreremin.predefense.registration.sys.models.Commission;
import ru.dreremin.predefense.registration.sys.security.ActorDetails;
import ru.dreremin.predefense.registration.sys.services.commission
		 .ReadCommissionService;

//@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
@RequiredArgsConstructor
@Slf4j
@RestController
public class ReadCommissionController {
	
	private final ReadCommissionService service;
	
	@GetMapping("/student/commissions/read/current")
	public ResponseEntity<CurrentCommissionResponseDto> 
			getCurrentComissionOfStudent() {
		
		CurrentCommissionResponseDto responseDto = 
				service.getCurrentComissionOfStudent();
		
		log.info("ReadComissionController.getCurrentComissionOfStudent() "
				+ "is success");
		return ResponseEntity.ok(responseDto);
	}
	
	@GetMapping("/student/commissions/read/actual-list")
	public ResponseEntity<WrapperForPageResponseDto
			<Commission, CommissionResponseDto>> 
					getActualComissionsListForStudent(
							@RequestParam(value = "page", defaultValue = "0") 
							@Min(0)
							@Max(Integer.MAX_VALUE)
							int page, 
							@RequestParam(value = "size", defaultValue = "10") 
							@Min(0)
							@Max(Integer.MAX_VALUE)
							int size) {
		
		WrapperForPageResponseDto<Commission, CommissionResponseDto> 
				result = service.getActualComissionsListForStudent(
						PageRequest.of(
								page, 
								size, 
								Sort.by(Sort.Order
										.asc("startDateTime")
										.nullsLast())));
		
		log.info("ReadComissionController.getActualComissionsListForStudent()"
				+ "is success");
		return ResponseEntity.ok(result);
	}

	@GetMapping("/teacher/commissions/read/actual-list")
	public ResponseEntity<WrapperForPageResponseDto
			<Commission, CommissionResponseDto>>
					getActualComissionsListForTeacher(
							@RequestParam(value = "page", defaultValue = "0") 
							@Min(0)
							@Max(Integer.MAX_VALUE)
							int page, 
							@RequestParam(value = "size", defaultValue = "10") 
							@Min(0)
							@Max(Integer.MAX_VALUE)
							int size) {
		
		WrapperForPageResponseDto<Commission, CommissionResponseDto> result = 
				service.getActualComissionsListForTeacher(PageRequest.of(
						page, 
						size, 
						Sort.by(Sort.Order.asc("startDateTime").nullsLast())));
		
		log.info("ReadComissionController.getActualComissionsListForTeacher()"
				+ "is success");
		return ResponseEntity.ok(result);
	}
	
	@PostMapping(
			value = "/admin/commissions/read/list-by-period", 
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<WrapperForPageResponseDto
			<Commission, CommissionResponseDto>> getComissionsListByTimePeriod(
					@RequestParam(value = "page", defaultValue = "0") 
					@Min(0)
					@Max(Integer.MAX_VALUE)
					int page, 
					@RequestParam(value = "size", defaultValue = "10") 
					@Min(0)
					@Max(Integer.MAX_VALUE)
					int size,
					@Valid @RequestBody TimePeriodRequestDto dto) {
		
		dto.periodValidation();
		WrapperForPageResponseDto<Commission, CommissionResponseDto> result = 
				service.getCommissionListByTimePeriod(
						dto, 
						PageRequest.of(
								page, 
								size, 
								Sort.by(Sort.Order
										.asc("startDateTime")
										.nullsLast())));
		log.info("ReadComissionController.getComissionsListByTimePeriod()"
				+ "is success");
		return ResponseEntity.ok(result);
	}
	
	@GetMapping(value = "/admin/commissions/read/by-id/{id}")
	public ResponseEntity<CommissionResponseDto> 
			getComissionById(
					@PathVariable("id")
					@Min(1) 
					@Max(Integer.MAX_VALUE) 
					int id) {
		
		CommissionResponseDto commission = service
				.getCommissionById(id);
		
		log.info("ReadComissionController.getCommissionById() is success");
		return ResponseEntity.ok(commission);
	}
}
