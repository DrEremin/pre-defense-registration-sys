package ru.dreremin.predefense.registration.sys.controllers.teacher;

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

import ru.dreremin.predefense.registration.sys.dto.request.TeacherRequestDto;
import ru.dreremin.predefense.registration.sys.dto.response.StatusResponseDto;
import ru.dreremin.predefense.registration.sys.services.teacher
		 .UpdateTeacherService;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UpdateTeacherController {
	
	private final UpdateTeacherService updateTeacherService;
	
	@PatchMapping(
			value = "/teacher/{id}",
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<StatusResponseDto> updateTeacher(
			@PathVariable(value = "id") @Min(1) @Max(Integer.MAX_VALUE) int id, 
			@RequestBody TeacherRequestDto dto) {
		
		updateTeacherService.updateTeacher(id, dto);
		log.info("UpdateTeacherController.updateTeacher() success");
		return ResponseEntity.ok(new StatusResponseDto(200, "Ok"));
	}
}
