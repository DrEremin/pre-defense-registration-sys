package ru.dreremin.predefense.registration.sys.controllers.create;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.dreremin.predefense.registration.sys.dto.requestdto.impl.StudentDto;
import ru.dreremin.predefense.registration.sys.dto.requestdto.impl.TeacherDto;
import ru.dreremin.predefense.registration.sys.dto.responsedto.StatusDto;
import ru.dreremin.predefense.registration.sys.exceptions.UniquenessViolationException;
import ru.dreremin.predefense.registration.sys
		 .services.students.CreateStudentService;
import ru.dreremin.predefense.registration.sys
		 .services.teachers.CreateTeacherService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/person-create")
public class CreatePersonController {
	
	private final CreateStudentService studentService;
	private final CreateTeacherService teacherService;
	
	@PutMapping(value = "/teacher", consumes = MediaType.APPLICATION_JSON_VALUE)
	public StatusDto createTeacher(@Valid @RequestBody TeacherDto teacher) 
			throws UniquenessViolationException,
				   MethodArgumentNotValidException {
		teacherService.createTeacher(teacher);
		log.info("CreatePersonController.createTeacher() success");
		return new StatusDto(200, "Ok");
	}
	
	@PutMapping(value = "/student", consumes = MediaType.APPLICATION_JSON_VALUE)
	public StatusDto createStudent(@Valid @RequestBody StudentDto student) 
			throws UniquenessViolationException {
		studentService.createStudent(student);
		log.info("CreatePersonController.createStudent() success");
		return new StatusDto(200, "Ok");
	}
}

