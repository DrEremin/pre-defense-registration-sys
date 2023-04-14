package ru.dreremin.predefense.registration.sys.controllers.teacher;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.dreremin.predefense.registration.sys.dto.request.TeacherRequestDto;
import ru.dreremin.predefense.registration.sys.dto.response.JwtTokenResponseDto;
import ru.dreremin.predefense.registration.sys.services.teacher.CreateTeacherService;

@Slf4j
@RequiredArgsConstructor
@RestController
public class CreateTeacherController {

	private final CreateTeacherService teacherService;
	
	@PutMapping(value = "/admin/users/create/teacher", 
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<JwtTokenResponseDto> createTeacher(
			@Valid @RequestBody TeacherRequestDto teacherRequestDto) {
		String jwtToken = teacherService.createTeacher(teacherRequestDto);
		log.info("CreateTeacherController.createTeacher() success");
		return ResponseEntity.ok(new JwtTokenResponseDto(jwtToken));
	}
}
