package ru.dreremin.predefense.registration.sys.controllers.student;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import ru.dreremin.predefense.registration.sys.dto.request.StudentRequestDto;
import ru.dreremin.predefense.registration.sys.dto.response.StatusResponseDto;
import ru.dreremin.predefense.registration.sys.services.student
		 .UpdateStudentService;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UpdateStudentController {

	private final UpdateStudentService updateStudentService;
	
	@PutMapping(
			value = "/admin/users/update/student/by-login/{login}",
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<StatusResponseDto> updateStudent(
			@PathVariable(value = "login") String login, 
			@RequestBody StudentRequestDto dto) {
		
		updateStudentService.updateStudent(login, dto);
		log.info("UpdateStudentController.updateStudent() success");
		return ResponseEntity.ok(new StatusResponseDto(200, "Ok"));
	}
}
