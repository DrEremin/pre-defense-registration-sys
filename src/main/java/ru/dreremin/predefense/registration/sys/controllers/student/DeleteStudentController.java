package ru.dreremin.predefense.registration.sys.controllers.student;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import ru.dreremin.predefense.registration.sys.dto.response.StatusResponseDto;
import ru.dreremin.predefense.registration.sys.services.student
		 .DeleteStudentService;

@Slf4j
@RequiredArgsConstructor
@RestController
public class DeleteStudentController {

	private final DeleteStudentService deleteStudentService;
	
	@DeleteMapping("/student")
	public ResponseEntity<StatusResponseDto> deleteAllStudents() {
		
		deleteStudentService.deleteAllStudents();
		log.info("DeleteStudentController.deleteAllStudents() is success");
		return ResponseEntity.ok(new StatusResponseDto(200, "Ok"));
	}
}

