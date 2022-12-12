package ru.dreremin.predefense.registration.sys.controllers.read;

import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.dreremin.predefense.registration.sys.dto.requestdto.AuthenticationDto;
import ru.dreremin.predefense.registration.sys.dto.responsedto
		 .ActualComissionForStudentDto;
import ru.dreremin.predefense.registration.sys.dto.responsedto
		 .ActualComissionForTeacherDto;
import ru.dreremin.predefense.registration.sys.dto.responsedto
		 .CurrentComissionOfStudentDto;
import ru.dreremin.predefense.registration.sys.exceptions
		 .FailedAuthenticationException;
import ru.dreremin.predefense.registration.sys.services.registrations
		 .ReadRegistrationService;

@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping(value = "/comissions-read")
public class ReadComissionController {
	/*
	private final ReadRegistrationService service;
	
	@PostMapping(value = "/current/student")
	public CurrentComissionOfStudentDto getCurrentComissionOfStudent(
			@Valid @RequestBody AuthenticationDto dto) 
					throws EntityNotFoundException, 
					FailedAuthenticationException,
					MethodArgumentNotValidException,
					HttpMessageNotReadableException {
		
		CurrentComissionOfStudentDto responseDto = 
				service.getCurrentComissionOfStudent(dto);
		
		log.info("ReadComissionController.getComissionForStudent() success");
		return responseDto;
	}
	
	@PostMapping(value = "/actual/student")
	public List<ActualComissionForStudentDto> 
			getActualComissionsListForStudent(
					@Valid @RequestBody AuthenticationDto dto) 
							throws EntityNotFoundException, 
							FailedAuthenticationException,
							MethodArgumentNotValidException,
							HttpMessageNotReadableException {
		
		List<ActualComissionForStudentDto> actualComissions = service
				.getActualComissionsListForStudent(dto);
		
		log.info("ReadComissionController.getActualComissionsListForStudent()"
				+ " success");
		return actualComissions;
	}

	@PostMapping(value = "/actual/teacher")
	public List<ActualComissionForTeacherDto> 
			getActualComissionsListForTeacher(
					@Valid @RequestBody AuthenticationDto dto) 
							throws EntityNotFoundException, 
							FailedAuthenticationException,
							MethodArgumentNotValidException,
							HttpMessageNotReadableException {
		
		List<ActualComissionForTeacherDto> actualComissions = service
				.getActualComissionsListForTeacher(dto);
		
		log.info("ReadComissionController.getActualComissionsListForTeacher()"
				+ " success");
		return actualComissions;
	}*/
}
