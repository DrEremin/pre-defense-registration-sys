package ru.dreremin.predefense.registration.sys.controllers.delete;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import ru.dreremin.predefense.registration.sys.dto.requestdto.LoginDto;
import ru.dreremin.predefense.registration.sys.dto.responsedto.StatusDto;
import ru.dreremin.predefense.registration.sys.services.admins.DeleteAdministratorService;
import ru.dreremin.predefense.registration.sys.services.students.DeleteStudentService;
import ru.dreremin.predefense.registration.sys.services.teachers.DeleteTeacherService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/users/delete")
public class DeleteUserController {

	private final DeleteStudentService deleteStudentService;
	private final DeleteTeacherService deleteTeacherService;
	private final DeleteAdministratorService deleteAdministratorService;
	
	@DeleteMapping("/student")
	public ResponseEntity<StatusDto> deleteStudent(
			@Valid @RequestBody LoginDto dto) {
		
		deleteStudentService.deleteStudent(dto.getLogin());
		log.info("DeletePersonController.deleteStudent() is success");
		return ResponseEntity.ok(new StatusDto(200, "Ok"));
	}
	
	@DeleteMapping("/teacher")
	public ResponseEntity<StatusDto> deleteTeacher(
			@Valid @RequestBody LoginDto dto) {
		
		deleteTeacherService.deleteTeacher(dto.getLogin());
		log.info("DeletePersonController.deleteTeacher() is success");
		return ResponseEntity.ok(new StatusDto(200, "Ok"));
	}
	
	@DeleteMapping("/admin")
	public ResponseEntity<StatusDto> deleteAdmin(
			@Valid @RequestBody LoginDto dto) {
		
		deleteAdministratorService.deleteAdmin(dto.getLogin());
		log.info("DeletePersonController.deleteAdmin() is success");
		return ResponseEntity.ok(new StatusDto(200, "Ok"));
	}
}
