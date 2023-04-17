package ru.dreremin.predefense.registration.sys.controllers.teacher;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.dreremin.predefense.registration.sys.dto.request.TeacherRequestDto;
import ru.dreremin.predefense.registration.sys.dto.response.StatusResponseDto;
import ru.dreremin.predefense.registration.sys.services.teacher
		 .UpdateTeacherService;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UpdateTeacherController {
	
	private final UpdateTeacherService updateTeacherService;
	
	@PutMapping(
			value = "/admin/users/update/teacher/by-login/{login}",
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<StatusResponseDto> updateTeacher(
			@PathVariable(value = "login") String login, 
			@RequestBody TeacherRequestDto dto) {
		
		updateTeacherService.updateTeacher(login, dto);
		log.info("UpdateTeacherController.updateTeacher() success");
		return ResponseEntity.ok(new StatusResponseDto(200, "Ok"));
	}
}
