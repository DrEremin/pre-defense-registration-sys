package ru.dreremin.predefense.registration.sys.controllers.teacher;

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
	
	@PutMapping(value = "/admin/users/update/teacher/{login}")
	public ResponseEntity<StatusResponseDto> updateTeacher(
			@PathVariable(value = "login") String login, 
			@RequestBody TeacherRequestDto dto) {
		
		updateTeacherService.updateTeacher(login, dto);
		log.info("ReadTeacherController.getAllTeachers() success");
		return ResponseEntity.ok(new StatusResponseDto(200, "Ok"));
	}
}
