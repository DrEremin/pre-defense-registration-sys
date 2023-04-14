package ru.dreremin.predefense.registration.sys.controllers.student;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.dreremin.predefense.registration.sys.dto.request.StudentRequestDto;
import ru.dreremin.predefense.registration.sys.dto.response.JwtTokenResponseDto;
import ru.dreremin.predefense.registration.sys.services.student.CreateStudentService;


@Slf4j
@RequiredArgsConstructor
@RestController
public class CreateStudentController {

	private final CreateStudentService studentService;
	
	@PutMapping(value = "/admin/users/create/student", 
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<JwtTokenResponseDto> createStudent(
			@Valid @RequestBody StudentRequestDto studentRequestDto) {
		String jwtToken = studentService.createStudent(studentRequestDto);
		log.info("CreateStudentController.createStudent() success");
		return ResponseEntity.ok(new JwtTokenResponseDto(jwtToken));
	}
}
