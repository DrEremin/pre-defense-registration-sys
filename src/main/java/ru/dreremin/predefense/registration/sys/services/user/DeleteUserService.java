package ru.dreremin.predefense.registration.sys.services.user;

import javax.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import ru.dreremin.predefense.registration.sys.models.Actor;
import ru.dreremin.predefense.registration.sys.repositories.ActorRepository;
import ru.dreremin.predefense.registration.sys.services.admin
		 .DeleteAdministratorService;
import ru.dreremin.predefense.registration.sys.services.student
		 .DeleteStudentService;
import ru.dreremin.predefense.registration.sys.services.teacher
		 .DeleteTeacherService;
import ru.dreremin.predefense.registration.sys.util.enums.Role;

@RequiredArgsConstructor
@Service
public class DeleteUserService {
	
	private final DeleteTeacherService deleteTeacherService;
	
	private final DeleteStudentService deleteStudentService;
	
	private final DeleteAdministratorService deleteAdministratorService;
	
	private final ActorRepository actorRepository;
	
	public void deleteUser(long actorId) {
		
		Actor actor = actorRepository.findById(actorId).orElseThrow(
				() -> new EntityNotFoundException(
						"User with this ID does not exists"));
		if (actor.getRole().equals(Role.STUDENT.getRole())) {
			deleteStudentService.deleteStudentById(actor.getId());
		} else if (actor.getRole().equals(Role.TEACHER.getRole())) {
			deleteTeacherService.deleteTeacherById(actor.getId());
		} else if (actor.getRole().equals(Role.ADMIN.getRole())) {
			deleteAdministratorService.deleteAdminById(actorId);
		}
	}
}
