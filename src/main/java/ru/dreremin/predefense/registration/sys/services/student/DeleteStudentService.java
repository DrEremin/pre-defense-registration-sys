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
import ru.dreremin.predefense.registration.sys.repositories
		 .StudentCommissionRepository;
import ru.dreremin.predefense.registration.sys.repositories.StudentRepository;
import ru.dreremin.predefense.registration.sys.services.person
		 .DeletePersonService;

@RequiredArgsConstructor
@Service
public class DeleteStudentService {
	
	private final ActorRepository actorRepositroy;
	private final StudentRepository studentRepository;
	private final StudentCommissionRepository studentComissionRepository;
	private final DeletePersonService deletePersonService;
	
	public void deleteStudentById(long actorId) {
		
		Optional<Actor> actorOpt = actorRepositroy.findById(actorId);
		
		if (actorOpt.isEmpty()) {
			throw new UsernameNotFoundException(
					"User with this ID does not exist");
		}
		
		Optional<Student> studentOpt = studentRepository
				.findByActorId(actorId);
		
		if (studentOpt.isEmpty()) {
			throw new EntityNotFoundException(
					"User with this ID is not a student");
		}
		
		deleteStudent(studentOpt.get().getId());
	}
	
	@Transactional(isolation = Isolation.SERIALIZABLE, 
			rollbackFor = { UsernameNotFoundException.class, 
					EntityNotFoundException.class })
	public void deleteAllStudents() {
		
		List<Student> students = studentRepository.findAll();
		
		for (Student student : students) {
			deleteStudent(student.getId());
		}
	}
	
	@Transactional(isolation = Isolation.SERIALIZABLE, 
			rollbackFor = { UsernameNotFoundException.class, 
					EntityNotFoundException.class })
	private void deleteStudent(long id) {

		Actor actor = actorRepositroy.findByStudentId(id).get();
		
		studentComissionRepository.deleteAllByStudentId(id);
		studentRepository.deleteById(id);
		deletePersonService.deletePersonAndEmail(actor.getId());
		actorRepositroy.delete(actor);
	}
}
