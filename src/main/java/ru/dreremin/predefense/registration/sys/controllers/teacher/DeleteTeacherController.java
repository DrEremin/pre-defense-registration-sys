package ru.dreremin.predefense.registration.sys.controllers.teacher;

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
	
	@DeleteMapping("/admin/users/delete/teacher/by-login/{login}")
	public ResponseEntity<StatusResponseDto> deleteTeacher(
			@PathVariable(value = "login") String login) {
		
		deleteTeacherService.deleteTeacher(login);
		log.info("DeleteTeacherController.deleteTeacher() is success");
		return ResponseEntity.ok(new StatusResponseDto(200, "Ok"));
	}
}
