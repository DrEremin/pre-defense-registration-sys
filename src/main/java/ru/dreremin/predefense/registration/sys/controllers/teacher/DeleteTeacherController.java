package ru.dreremin.predefense.registration.sys.controllers.teacher;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import ru.dreremin.predefense.registration.sys.dto.response.StatusResponseDto;
import ru.dreremin.predefense.registration.sys.services.teacher
		 .DeleteTeacherService;

@Slf4j
@RequiredArgsConstructor
@RestController
public class DeleteTeacherController {

	private final DeleteTeacherService deleteTeacherService;
	
	@DeleteMapping("/admin/users/delete/teacher/by-id/{id}")
	public ResponseEntity<StatusResponseDto> deleteTeacherById(
			@PathVariable(value = "id") @Min(1) @Max(Long.MAX_VALUE) long id) {
		
		deleteTeacherService.deleteTeacherById(id);
		log.info("DeleteTeacherController.deleteTeacherById() is success");
		return ResponseEntity.ok(new StatusResponseDto(200, "Ok"));
	}
}
