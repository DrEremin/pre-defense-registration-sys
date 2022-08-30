package ru.dreremin.predefense.registration.sys.controllers.read;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import ru.dreremin.predefense.registration.sys.dto.requestdto.impl
		 .AuthenticationDto;
import ru.dreremin.predefense.registration.sys.dto.responsedto
		 .CurrentComissionOfStudentDto;
import ru.dreremin.predefense.registration.sys.exceptions
		 .FailedAuthenticationException;
import ru.dreremin.predefense.registration.sys.services.registrations
		 .ReadRegistrationService;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping(value = "/comissions-read")
public class ReadComissionController {
	
	private final ReadRegistrationService service;
	
	@PostMapping(value = "/student")
	public CurrentComissionOfStudentDto getCurrentComissionOfStudent(
			@Valid @RequestBody AuthenticationDto dto) 
					throws EntityNotFoundException, 
					FailedAuthenticationException {
		
		CurrentComissionOfStudentDto responseDto = 
				service.getCurrentComissionOfStudent(dto);
		
		log.info("ReadComissionController.getComissionForStudent() success");
		return responseDto;
	}
	
	
	public void getActualComissionsForStudent() {
		
	}
	
}
