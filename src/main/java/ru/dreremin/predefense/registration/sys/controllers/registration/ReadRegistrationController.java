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

import ru.dreremin.predefense.registration.sys.dto.request.TimePeriodDto;
import ru.dreremin.predefense.registration.sys.dto.response.CommissionDto;
import ru.dreremin.predefense.registration.sys.dto.response
		 .CurrentCommissionOfStudentDto;
import ru.dreremin.predefense.registration.sys.services.registration
		 .ReadRegistrationService;

//@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
@RequiredArgsConstructor
@Slf4j
@RestController
public class ReadRegistrationController {
	
	private final ReadRegistrationService service;
	
	@GetMapping(value = "/student/commissions/read/current")
	public CurrentCommissionOfStudentDto getCurrentComissionOfStudent() {
		
		CurrentCommissionOfStudentDto responseDto = 
				service.getCurrentComissionOfStudent();
		
		log.info("ReadComissionController.getComissionForStudent() is success");
		return responseDto;
	}
	
	@GetMapping(value = "/student/commissions/read/actual-list")
	public List<CommissionDto> 
			getActualComissionsListForStudent() {
		
		List<CommissionDto> actualComissions = service
				.getActualComissionsListForStudent();
		
		log.info("ReadComissionController.getActualComissionsListForStudent()"
				+ "is success");
		return actualComissions;
	}

	@GetMapping(value = "/teacher/commissions/read/actual-list")
	public List<CommissionDto> 
			getActualComissionsListForTeacher() {
		
		List<CommissionDto> actualComissions = service
				.getActualComissionsListForTeacher();
		
		log.info("ReadComissionController.getActualComissionsListForTeacher()"
				+ "is success");
		return actualComissions;
	}
	
	@PostMapping(value = "/admin/commissions/read/list-by-period")
	public List<CommissionDto> 
			getComissionsListByTimePeriod(
					@Valid @RequestBody TimePeriodDto dto) {
		
		dto.periodValidation();
		List<CommissionDto> actualComissions = service
				.getCommissionListByTimePeriod(dto);
		
		log.info("ReadComissionController.getComissionsListByTimePeriod()"
				+ "is success");
		return actualComissions;
	}
	
	@GetMapping(value = "/admin/commissions/read/{id}")
	public List<CommissionDto> 
			getComissionById(@PathVariable("id") int id) {
		
		List<CommissionDto> commission = service
				.getCommissionById(id);
		
		log.info("ReadComissionController.getCommissionById()"
				+ "is success");
		return commission;
	}
}
