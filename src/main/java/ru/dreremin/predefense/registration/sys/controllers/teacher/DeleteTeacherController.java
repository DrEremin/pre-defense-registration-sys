package ru.dreremin.predefense.registration.sys.controllers.teacher;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.dreremin.predefense.registration.sys.dto.request.LoginDto;
import ru.dreremin.predefense.registration.sys.dto.response.StatusDto;
import ru.dreremin.predefense.registration.sys.services.teacher.DeleteTeacherService;

@Slf4j
@RequiredArgsConstructor
@RestController
public class DeleteTeacherController {

	private final DeleteTeacherService deleteTeacherService;
	
	@DeleteMapping("/admin/users/delete/teacher")
	public ResponseEntity<StatusDto> deleteTeacher(
			@Valid @RequestBody LoginDto dto) {
		
		deleteTeacherService.deleteTeacher(dto.getLogin());
		log.info("DeleteTeacherController.deleteTeacher() is success");
		return ResponseEntity.ok(new StatusDto(200, "Ok"));
	}
}
