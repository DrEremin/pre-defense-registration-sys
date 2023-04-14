package ru.dreremin.predefense.registration.sys.controllers.registration;

import java.util.List;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import ru.dreremin.predefense.registration.sys.dto.request.TimePeriodRequestDto;
import ru.dreremin.predefense.registration.sys.dto.response.CommissionResponseDto;
import ru.dreremin.predefense.registration.sys.dto.response
		 .CurrentCommissionResponseDto;
import ru.dreremin.predefense.registration.sys.services.registration
		 .ReadRegistrationService;

//@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
@RequiredArgsConstructor
@Slf4j
@RestController
public class ReadRegistrationController {
	
	private final ReadRegistrationService service;
	
	@GetMapping(value = "/student/commissions/read/current")
	public CurrentCommissionResponseDto getCurrentComissionOfStudent() {
		
		CurrentCommissionResponseDto responseDto = 
				service.getCurrentComissionOfStudent();
		
		log.info("ReadComissionController.getComissionForStudent() is success");
		return responseDto;
	}
	
	@GetMapping(value = "/student/commissions/read/actual-list")
	public List<CommissionResponseDto> 
			getActualComissionsListForStudent() {
		
		List<CommissionResponseDto> actualComissions = service
				.getActualComissionsListForStudent();
		
		log.info("ReadComissionController.getActualComissionsListForStudent()"
				+ "is success");
		return actualComissions;
	}

	@GetMapping(value = "/teacher/commissions/read/actual-list")
	public List<CommissionResponseDto> 
			getActualComissionsListForTeacher() {
		
		List<CommissionResponseDto> actualComissions = service
				.getActualComissionsListForTeacher();
		
		log.info("ReadComissionController.getActualComissionsListForTeacher()"
				+ "is success");
		return actualComissions;
	}
	
	@PostMapping(value = "/admin/commissions/read/list-by-period")
	public List<CommissionResponseDto> 
			getComissionsListByTimePeriod(
					@Valid @RequestBody TimePeriodRequestDto dto) {
		
		dto.periodValidation();
		List<CommissionResponseDto> actualComissions = service
				.getCommissionListByTimePeriod(dto);
		
		log.info("ReadComissionController.getComissionsListByTimePeriod()"
				+ "is success");
		return actualComissions;
	}
	
	@GetMapping(value = "/admin/commissions/read/{id}")
	public List<CommissionResponseDto> 
			getComissionById(@PathVariable("id") int id) {
		
		List<CommissionResponseDto> commission = service
				.getCommissionById(id);
		
		log.info("ReadComissionController.getCommissionById()"
				+ "is success");
		return commission;
	}
}
