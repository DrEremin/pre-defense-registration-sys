package ru.dreremin.predefense.registration.sys.controllers.registration;

import java.util.List;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import ru.dreremin.predefense.registration.sys.dto.request.TimePeriodDto;
import ru.dreremin.predefense.registration.sys.dto.response.CommissionsListDto;
import ru.dreremin.predefense.registration.sys.dto.response
		 .CurrentCommissionOfStudentDto;
import ru.dreremin.predefense.registration.sys.services.registration
		 .ReadRegistrationService;

//@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping(value = "/comissions/read")
public class ReadRegistrationController {
	
	private final ReadRegistrationService service;
	
	@GetMapping(value = "/student/current")
	public CurrentCommissionOfStudentDto getCurrentComissionOfStudent() {
		
		CurrentCommissionOfStudentDto responseDto = 
				service.getCurrentComissionOfStudent();
		
		log.info("ReadComissionController.getComissionForStudent() is success");
		return responseDto;
	}
	
	@GetMapping(value = "/student/actual-list")
	public List<CommissionsListDto> 
			getActualComissionsListForStudent() {
		
		List<CommissionsListDto> actualComissions = service
				.getActualComissionsListForStudent();
		
		log.info("ReadComissionController.getActualComissionsListForStudent()"
				+ "is success");
		return actualComissions;
	}

	@GetMapping(value = "/teacher/actual-list")
	public List<CommissionsListDto> 
			getActualComissionsListForTeacher() {
		
		List<CommissionsListDto> actualComissions = service
				.getActualComissionsListForTeacher();
		
		log.info("ReadComissionController.getActualComissionsListForTeacher()"
				+ "is success");
		return actualComissions;
	}
	
	@PostMapping(value = "/admin/list-by-period")
	public List<CommissionsListDto> 
			getComissionsListByTimePeriod(
					@Valid @RequestBody TimePeriodDto dto) {
		
		dto.periodValidation();
		List<CommissionsListDto> actualComissions = service
				.getCommissionListByTimePeriod(dto);
		
		log.info("ReadComissionController.getComissionsListByTimePeriod()"
				+ "is success");
		return actualComissions;
	}
}
