package ru.dreremin.predefense.registration.sys.controllers.commission;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
		 .WrapperForListResponseDto;
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
	public ResponseEntity<WrapperForListResponseDto<CommissionResponseDto>> 
			getActualComissionsListForStudent() {
		
		WrapperForListResponseDto<CommissionResponseDto> actualComissions = 
				service.getActualComissionsListForStudent();
		
		log.info("ReadComissionController.getActualComissionsListForStudent()"
				+ "is success");
		return ResponseEntity.ok(actualComissions);
	}

	@GetMapping("/teacher/commissions/read/actual-list")
	public ResponseEntity<WrapperForListResponseDto<CommissionResponseDto>>
			getActualComissionsListForTeacher() {
		
		WrapperForListResponseDto<CommissionResponseDto> actualComissions = 
				service.getActualComissionsListForTeacher();
		
		log.info("ReadComissionController.getActualComissionsListForTeacher()"
				+ "is success");
		return ResponseEntity.ok(actualComissions);
	}
	
	@PostMapping(
			value = "/admin/commissions/read/list-by-period", 
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<WrapperForListResponseDto<CommissionResponseDto>> 
			getComissionsListByTimePeriod(
					@Valid @RequestBody TimePeriodRequestDto dto) {
		
		dto.periodValidation();
		WrapperForListResponseDto<CommissionResponseDto> actualComissions = 
				service.getCommissionListByTimePeriod(dto);
		
		log.info("ReadComissionController.getComissionsListByTimePeriod()"
				+ "is success");
		return ResponseEntity.ok(actualComissions);
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
