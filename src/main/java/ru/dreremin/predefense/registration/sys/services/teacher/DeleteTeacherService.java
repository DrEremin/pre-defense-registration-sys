package ru.dreremin.predefense.registration.sys.services.teacher;

import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import ru.dreremin.predefense.registration.sys.models.Actor;
import ru.dreremin.predefense.registration.sys.models.Teacher;
import ru.dreremin.predefense.registration.sys.repositories.ActorRepository;
import ru.dreremin.predefense.registration.sys.repositories
		 .TeacherCommissionRepository;
import ru.dreremin.predefense.registration.sys.repositories.TeacherRepository;
import ru.dreremin.predefense.registration.sys.services.person
		 .DeletePersonService;

@RequiredArgsConstructor
@Service
public class DeleteTeacherService {

	private final ActorRepository actorRepository;
	private final TeacherRepository teacherRepostory;
	private final TeacherCommissionRepository teacherComissionRepository;
	private final DeletePersonService deletePersonService;
	
	@Transactional(isolation = Isolation.SERIALIZABLE, 
			rollbackFor = { UsernameNotFoundException.class, 
					EntityNotFoundException.class })
	public void deleteTeacherById(long actorId) {
		
		Optional<Actor> actorOpt = actorRepository.findById(actorId);
		
		if (actorOpt.isEmpty()) {
			throw new UsernameNotFoundException(
					"User with this ID does not exist");
		}
		
		Optional<Teacher> teacherOpt = teacherRepostory.findByActorId(actorId);
		
		if (teacherOpt.isEmpty()) {
			throw new EntityNotFoundException(
					"User with this ID is not a teacher");
		}
		teacherComissionRepository.deleteAllByTeacherId(
				teacherOpt.get().getId());
		teacherRepostory.delete(teacherOpt.get());
		deletePersonService.deletePersonAndEmail(actorOpt.get().getId());
		actorRepository.delete(actorOpt.get());
	}
}
