package ru.dreremin.predefense.registration.sys.controllers.create;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.dreremin.predefense.registration.sys.dto.requestdto.AdminDto;
import ru.dreremin.predefense.registration.sys.dto.requestdto.StudentDto;
import ru.dreremin.predefense.registration.sys.dto.requestdto.TeacherDto;
import ru.dreremin.predefense.registration.sys.dto.responsedto.JwtTokenDto;
import ru.dreremin.predefense.registration.sys.exceptions.UniquenessViolationException;
import ru.dreremin.predefense.registration.sys.services.admins.CreateAdministratorService;
import ru.dreremin.predefense.registration.sys
		 .services.students.CreateStudentService;
import ru.dreremin.predefense.registration.sys
		 .services.teachers.CreateTeacherService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/users/create")
public class CreateUserController {
	
	private final CreateStudentService studentService;
	private final CreateTeacherService teacherService;
	private final CreateAdministratorService adminService;
	
	@PutMapping(value = "/teacher", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<JwtTokenDto> createTeacher(
			@Valid @RequestBody TeacherDto teacherDto) {
		String jwtToken = teacherService.createTeacher(teacherDto);
		log.info("CreatePersonController.createTeacher() success");
		return ResponseEntity.ok(new JwtTokenDto(jwtToken));
	}
	
	@PutMapping(value = "/student", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<JwtTokenDto> createStudent(
			@Valid @RequestBody StudentDto studentDto) {
		String jwtToken = studentService.createStudent(studentDto);
		log.info("CreatePersonController.createStudent() success");
		return ResponseEntity.ok(new JwtTokenDto(jwtToken));
	}
	
	@PutMapping(value = "/admin", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<JwtTokenDto> createAdmin(
			@Valid @RequestBody AdminDto adminDto) {
		String jwtToken = adminService.createAdmin(adminDto);
		log.info("CreatePersonController.createStudent() success");
		return ResponseEntity.ok(new JwtTokenDto(jwtToken));
	}
}

