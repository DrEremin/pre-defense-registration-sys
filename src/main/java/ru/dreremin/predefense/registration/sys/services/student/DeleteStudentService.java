package ru.dreremin.predefense.registration.sys.services.student;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import ru.dreremin.predefense.registration.sys.models.Actor;
import ru.dreremin.predefense.registration.sys.models.Student;
import ru.dreremin.predefense.registration.sys.repositories.ActorRepository;
import ru.dreremin.predefense.registration.sys.repositories.StudentCommissionRepository;
import ru.dreremin.predefense.registration.sys.repositories.StudentRepository;
import ru.dreremin.predefense.registration.sys.services.person.DeletePersonService;

@RequiredArgsConstructor
@Service
public class DeleteStudentService {
	
	private final ActorRepository actorRepo;
	private final StudentRepository studentRepo;
	private final StudentCommissionRepository studentComissionRepo;
	private final DeletePersonService deletePersonService;
	
	public void deleteStudentByLogin(String login) {
		
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
		
		deleteStudent(studentOpt.get().getId());
	}
	
	@Transactional(isolation = Isolation.SERIALIZABLE, 
			rollbackFor = { UsernameNotFoundException.class, 
					EntityNotFoundException.class })
	public void deleteAllStudents() {
		
		List<Student> students = studentRepo.findAll();
		
		for (Student student : students) {
			deleteStudent(student.getId());
		}
	}
	
	@Transactional(isolation = Isolation.SERIALIZABLE, 
			rollbackFor = { UsernameNotFoundException.class, 
					EntityNotFoundException.class })
	private void deleteStudent(long id) {

		Actor actor = actorRepo.findByStudentId(id).get();
		
		studentComissionRepo.deleteAllByStudentId(id);
		studentRepo.deleteById(id);
		deletePersonService.deletePersonAndEmail(actor.getId());
		actorRepo.delete(actor);
	}
}
