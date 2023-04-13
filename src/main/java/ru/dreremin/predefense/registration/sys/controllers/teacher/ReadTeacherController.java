package ru.dreremin.predefense.registration.sys.controllers.teacher;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.dreremin.predefense.registration.sys.dto.response.TeacherDto;
import ru.dreremin.predefense.registration.sys.services.teacher.ReadTeacherService;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ReadTeacherController {

	private final ReadTeacherService readTeacherService;
	
	@GetMapping("/admin/users/read/teachers/all")
	public ResponseEntity<List<TeacherDto>> getAllTeachers() {
		
		List<TeacherDto> teachers = readTeacherService.getAllTeachers();
		
		log.info("ReadTeacherController.getAllTeachers() success");
		return ResponseEntity.ok(teachers);
	}
}