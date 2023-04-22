package ru.dreremin.predefense.registration.sys.controllers.student;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import ru.dreremin.predefense.registration.sys.dto.request.StudentRequestDto;
import ru.dreremin.predefense.registration.sys.dto.response.StatusResponseDto;
import ru.dreremin.predefense.registration.sys.services.student
		 .UpdateStudentService;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UpdateStudentController {

	private final UpdateStudentService updateStudentService;
	
	@PatchMapping(
			value = "/users/student/update/{id}",
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<StatusResponseDto> updateStudent(
			@PathVariable(value = "id") @Min(1) @Max(Long.MAX_VALUE) long id, 
			@RequestBody StudentRequestDto dto) {
		
		updateStudentService.updateStudent(id, dto);
		log.info("UpdateStudentController.updateStudent() success");
		return ResponseEntity.ok(new StatusResponseDto(200, "Ok"));
	}
}
