package ru.dreremin.predefense.registration.sys.controllers.student;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.dreremin.predefense.registration.sys.dto.response.StudentResponseDto;
import ru.dreremin.predefense.registration.sys.services.student
		 .ReadStudentService;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ReadStudentController {

	private final ReadStudentService readStudentService;
	
	@GetMapping("/admin/users/read/students/all")
	public ResponseEntity<List<StudentResponseDto>> getAllStudents() {
		
		List<StudentResponseDto> students = readStudentService.getAllStudents();
		
		log.info("ReadStudentController.getAllStudents() success");
		return ResponseEntity.ok(students);
	}
	
	@GetMapping("/admin/users/read/students/{groupNumber}")
	public ResponseEntity<List<StudentResponseDto>> getAllStudentsByGroupNumber(
			@PathVariable ("groupNumber") String groupNumber) {
		
		List<StudentResponseDto> students = readStudentService
				.getAllStudentsByGroupNumber(groupNumber);
		
		log.info("ReadStudentController.getAllStudents() success");
		return ResponseEntity.ok(students);
	}
}
