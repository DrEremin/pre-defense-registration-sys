package ru.dreremin.predefense.registration.sys.controllers.delete;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
	
	@GetMapping("/student")
	public ResponseEntity<StatusDto> deleteStudent(@RequestParam String login) {
		
		deleteStudentService.deleteStudent(login);
		log.info("DeletePersonController.deleteStudent() is success");
		return ResponseEntity.ok(new StatusDto(200, "Ok"));
	}
	
	@GetMapping("/teacher")
	public ResponseEntity<StatusDto> deleteTeacher(@RequestParam String login) {
		
		deleteTeacherService.deleteTeacher(login);
		log.info("DeletePersonController.deleteTeacher() is success");
		return ResponseEntity.ok(new StatusDto(200, "Ok"));
	}
	
	@GetMapping("/admin")
	public ResponseEntity<StatusDto> deleteAdmin(@RequestParam String login) {
		
		deleteAdministratorService.deleteAdmin(login);
		log.info("DeletePersonController.deleteAdmin() is success");
		return ResponseEntity.ok(new StatusDto(200, "Ok"));
	}
}
