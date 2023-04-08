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
import ru.dreremin.predefense.registration.sys.dto.requestdto.TeacherDto;
import ru.dreremin.predefense.registration.sys.dto.responsedto.JwtTokenDto;
import ru.dreremin.predefense.registration.sys
		 .services.teachers.CreateTeacherService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/users/create")
public class CreateTeacherController {

	private final CreateTeacherService teacherService;
	
	@PutMapping(value = "/teacher", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<JwtTokenDto> createTeacher(
			@Valid @RequestBody TeacherDto teacherDto) {
		String jwtToken = teacherService.createTeacher(teacherDto);
		log.info("CreateTeacherController.createTeacher() success");
		return ResponseEntity.ok(new JwtTokenDto(jwtToken));
	}
}
