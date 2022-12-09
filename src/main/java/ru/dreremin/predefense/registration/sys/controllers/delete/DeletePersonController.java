package ru.dreremin.predefense.registration.sys.controllers.delete;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.dreremin.predefense.registration.sys.dto.responsedto.StatusDto;
import ru.dreremin.predefense.registration.sys.services.students.DeleteStudentService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/persons/delete")
public class DeletePersonController {

	private final DeleteStudentService deleteStudentService;
	/*
	@DeleteMapping("/student")
	public ResponseEntity<StatusDto> deleteStudent(@RequestParam String login) {
		
	}*/
}
