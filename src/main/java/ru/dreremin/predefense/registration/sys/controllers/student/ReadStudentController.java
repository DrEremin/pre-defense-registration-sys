package ru.dreremin.predefense.registration.sys.controllers.student;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import ru.dreremin.predefense.registration.sys.dto.response.StudentResponseDto;
import ru.dreremin.predefense.registration.sys.dto.response
		 .WrapperForPageResponseDto;
import ru.dreremin.predefense.registration.sys.models.Student;
import ru.dreremin.predefense.registration.sys.services.student
		 .ReadStudentService;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ReadStudentController {

	private final ReadStudentService readStudentService;
	
	@GetMapping("/student/list")
	public ResponseEntity<WrapperForPageResponseDto<
			Student, StudentResponseDto>> getAllStudents(
					@RequestParam(value = "page", defaultValue = "0") 
					@Min(0)
					@Max(Integer.MAX_VALUE)
					int page, 
					@RequestParam(value = "size", defaultValue = "10") 
					@Min(0)
					@Max(Integer.MAX_VALUE)
					int size) {
		
		WrapperForPageResponseDto<Student, StudentResponseDto> students = 
				readStudentService.getAllStudents(PageRequest.of(page, size));
		
		log.info("ReadStudentController.getAllStudents() success");
		return ResponseEntity.ok(students);
	}
}