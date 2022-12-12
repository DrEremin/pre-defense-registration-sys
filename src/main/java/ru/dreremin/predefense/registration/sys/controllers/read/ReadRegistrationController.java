package ru.dreremin.predefense.registration.sys.controllers.read;

import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.dreremin.predefense.registration.sys.dto.requestdto.AuthenticationDto;
import ru.dreremin.predefense.registration.sys.dto.responsedto
		 .ActualCommissionForStudentDto;
import ru.dreremin.predefense.registration.sys.dto.responsedto
		 .ActualComissionForTeacherDto;
import ru.dreremin.predefense.registration.sys.dto.responsedto
		 .CurrentCommissionOfStudentDto;
import ru.dreremin.predefense.registration.sys.exceptions
		 .FailedAuthenticationException;
import ru.dreremin.predefense.registration.sys.services.registrations
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
	public List<ActualCommissionForStudentDto> 
			getActualComissionsListForStudent() {
		
		List<ActualCommissionForStudentDto> actualComissions = service
				.getActualComissionsListForStudent();
		
		log.info("ReadComissionController.getActualComissionsListForStudent()"
				+ "is success");
		return actualComissions;
	}

	@GetMapping(value = "/teacher/actual-list")
	public List<ActualComissionForTeacherDto> 
			getActualComissionsListForTeacher() {
		
		List<ActualComissionForTeacherDto> actualComissions = service
				.getActualComissionsListForTeacher();
		
		log.info("ReadComissionController.getActualComissionsListForTeacher()"
				+ "is success");
		return actualComissions;
	}
}
