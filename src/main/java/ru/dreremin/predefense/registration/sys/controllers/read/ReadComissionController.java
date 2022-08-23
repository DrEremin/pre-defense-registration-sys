package ru.dreremin.predefense.registration.sys.controllers.read;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;

import ru.dreremin.predefense.registration.sys.dto.requestdto.impl
		 .AuthorizationDto;

@Slf4j
@RestController
@RequestMapping(value = "/comission-read")
public class ReadComissionController {
	
	@PostMapping(value = "/student")
	public void getComissionForStudent(
			@Valid @RequestBody AuthorizationDto dto) {
		
	}
	
	public void getActualComissionsForStudent() {
		
	}
	
	//public void get
}
