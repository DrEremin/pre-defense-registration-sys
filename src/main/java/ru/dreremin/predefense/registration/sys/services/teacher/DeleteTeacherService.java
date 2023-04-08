package ru.dreremin.predefense.registration.sys.services.teacher;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ru.dreremin.predefense.registration.sys.models.Actor;
import ru.dreremin.predefense.registration.sys.models.Person;
import ru.dreremin.predefense.registration.sys.models.Teacher;
import ru.dreremin.predefense.registration.sys.repositories.ActorRepository;
import ru.dreremin.predefense.registration.sys.repositories.PersonRepository;
import ru.dreremin.predefense.registration.sys.repositories.TeacherCommissionRepository;
import ru.dreremin.predefense.registration.sys.repositories.TeacherRepository;
import ru.dreremin.predefense.registration.sys.services.person.DeletePersonService;

@RequiredArgsConstructor
@Service
public class DeleteTeacherService {

	private final ActorRepository actorRepo;
	private final TeacherRepository teacherRepo;
	private final TeacherCommissionRepository teacherComissionRepo;
	private final DeletePersonService deletePersonService;
	
	@Transactional(isolation = Isolation.SERIALIZABLE, 
			rollbackFor = { UsernameNotFoundException.class, 
					EntityNotFoundException.class })
	public void deleteTeacher(String login) {
		
		Optional<Actor> actorOpt = actorRepo.findByLogin(login);
		
		if (actorOpt.isEmpty()) {
			throw new UsernameNotFoundException(
					"User with this login does not exist");
		}
		
		Optional<Teacher> teacherOpt = teacherRepo.findByActorLogin(login);
		
		if (teacherOpt.isEmpty()) {
			throw new EntityNotFoundException(
					"Teacher with this login does not exist");
		}
		teacherComissionRepo.deleteAllByTeacherId(teacherOpt.get().getId());
		teacherRepo.delete(teacherOpt.get());
		deletePersonService.deletePersonAndEmail(actorOpt.get().getId());
		actorRepo.delete(actorOpt.get());
	}
}
