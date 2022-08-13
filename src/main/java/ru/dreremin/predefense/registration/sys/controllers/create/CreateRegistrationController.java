package ru.dreremin.predefense.registration.sys.controllers.create;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.dreremin.predefense.registration.sys.dto.requestdto.impl.RegistrationDto;
import ru.dreremin.predefense.registration.sys.dto.responsedto.StatusDto;

@RestController
@RequestMapping("/registration")
public class CreateRegistrationController {

	@PutMapping("/student")
	public StatusDto studentRegistration(
			@Valid @RequestBody RegistrationDto dto) {
		//student registration record creation service
		return new StatusDto(200, "Ok");
	}
	
	@PutMapping("/teacher")
	public StatusDto teacherRegistration(
			@Valid @RequestBody RegistrationDto dto) {
		//student registration record creation service
		return new StatusDto(200, "Ok");
	}
}
