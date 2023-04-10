package ru.dreremin.predefense.registration.sys.controllers.student;

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
import ru.dreremin.predefense.registration.sys.services.student.DeleteStudentService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/users/delete")
public class DeleteStudentController {

	private final DeleteStudentService deleteStudentService;
	
	@DeleteMapping("/student")
	public ResponseEntity<StatusDto> deleteStudent(
			@Valid @RequestBody LoginDto dto) {
		
		deleteStudentService.deleteStudentByLogin(dto.getLogin());
		log.info("DeleteStudentController.deleteStudent() is success");
		return ResponseEntity.ok(new StatusDto(200, "Ok"));
	}
	
	@DeleteMapping("/students/all")
	public ResponseEntity<StatusDto> deleteAllStudents() {
		
		deleteStudentService.deleteAllStudents();
		log.info("DeleteStudentController.deleteAllStudents() is success");
		return ResponseEntity.ok(new StatusDto(200, "Ok"));
	}
}

