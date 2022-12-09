package ru.dreremin.predefense.registration.sys.services.students;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ru.dreremin.predefense.registration.sys.exceptions.UniquenessViolationException;
import ru.dreremin.predefense.registration.sys.models.Actor;
import ru.dreremin.predefense.registration.sys.models.Person;
import ru.dreremin.predefense.registration.sys.models.Student;
import ru.dreremin.predefense.registration.sys.repositories.ActorRepository;
import ru.dreremin.predefense.registration.sys.repositories.PersonRepository;
import ru.dreremin.predefense.registration.sys.repositories.StudentComissionRepository;
import ru.dreremin.predefense.registration.sys.repositories.StudentRepository;
import ru.dreremin.predefense.registration.sys.services.persons.DeletePersonService;

@RequiredArgsConstructor
@Service
public class DeleteStudentService {
	
	private final ActorRepository actorRepo;
	private final StudentRepository studentRepo;
	private final StudentComissionRepository studentComissionRepo;
	private final DeletePersonService deletePersonService;
	
	@Transactional(isolation = Isolation.SERIALIZABLE, 
			rollbackFor = { UsernameNotFoundException.class, 
					EntityNotFoundException.class })
	public void deleteStudent(String login) {
		
		Optional<Actor> actorOpt = actorRepo.findByLogin(login);
		
		if (actorOpt.isEmpty()) {
			throw new UsernameNotFoundException(
					"User with this login does not exist");
		}
		
		Optional<Student> studentOpt = studentRepo.findByActorLogin(login);
		
		if (studentOpt.isEmpty()) {
			throw new EntityNotFoundException(
					"Student with this login does not exist");
		}
		studentComissionRepo.deleteAllByStudentId(studentOpt.get().getId());
		studentRepo.delete(studentOpt.get());
		deletePersonService.deletePersonAndEmail(actorOpt.get().getId());
	}
}
