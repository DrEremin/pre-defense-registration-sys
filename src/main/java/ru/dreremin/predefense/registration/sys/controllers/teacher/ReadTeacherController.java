package ru.dreremin.predefense.registration.sys.controllers.teacher;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.dreremin.predefense.registration.sys.dto.response.TeacherResponseDto;
import ru.dreremin.predefense.registration.sys.dto.response
		 .WrapperForPageResponseDto;
import ru.dreremin.predefense.registration.sys.models.Teacher;
import ru.dreremin.predefense.registration.sys.services.teacher
		 .ReadTeacherService;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ReadTeacherController {

	private final ReadTeacherService readTeacherService;
	
	@GetMapping("/users/teachers/read/all")
	public ResponseEntity<WrapperForPageResponseDto
			<Teacher, TeacherResponseDto>> getAllTeachers(
			@RequestParam(value = "page", defaultValue = "0") 
			@Min(0)
			@Max(Integer.MAX_VALUE)
			int page, 
			@RequestParam(value = "size", defaultValue = "10") 
			@Min(0)
			@Max(Integer.MAX_VALUE)
			int size) {
		
		WrapperForPageResponseDto<Teacher, TeacherResponseDto> teachers = 
				readTeacherService.getAllTeachers(PageRequest.of(page, size));
		log.info("ReadTeacherController.getAllTeachers() success");
		return ResponseEntity.ok(teachers);
	}
}
