package ru.dreremin.predefense.registration.sys.controllers.student;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.dreremin.predefense.registration.sys.dto.request.LoginRequestDto;
import ru.dreremin.predefense.registration.sys.dto.response.StatusResponseDto;
import ru.dreremin.predefense.registration.sys.services.student.DeleteStudentService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/users/delete")
public class DeleteStudentController {

	private final DeleteStudentService deleteStudentService;
	
	@DeleteMapping("/student/login")
	public ResponseEntity<StatusResponseDto> deleteStudent(
			@Valid @RequestBody LoginRequestDto dto) {
		
		deleteStudentService.deleteStudentByLogin(dto.getLogin());
		log.info("DeleteStudentController.deleteStudent() is success");
		return ResponseEntity.ok(new StatusResponseDto(200, "Ok"));
	}
	
	@DeleteMapping("/students/all")
	public ResponseEntity<StatusResponseDto> deleteAllStudents() {
		
		deleteStudentService.deleteAllStudents();
		log.info("DeleteStudentController.deleteAllStudents() is success");
		return ResponseEntity.ok(new StatusResponseDto(200, "Ok"));
	}
	
	@DeleteMapping("/students/{groupNumber}")
	public ResponseEntity<StatusResponseDto> deleteStudentsByGroup(
			@PathVariable("groupNumber") String group) {
		
		deleteStudentService.deleteStudentsByGroup(group);
		log.info("DeleteStudentController.deleteStudentsByGroup() is success");
		return ResponseEntity.ok(new StatusResponseDto(200, "Ok"));
	}
}

