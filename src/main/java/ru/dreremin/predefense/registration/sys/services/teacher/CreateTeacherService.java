package ru.dreremin.predefense.registration.sys.services.teacher;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import ru.dreremin.predefense.registration.sys.dto.request.TeacherRequestDto;
import ru.dreremin.predefense.registration.sys.exceptions
		 .UniquenessViolationException;
import ru.dreremin.predefense.registration.sys.models.Teacher;
import ru.dreremin.predefense.registration.sys.repositories.TeacherRepository;
import ru.dreremin.predefense.registration.sys.services.person
		 .CreatePersonService;
import ru.dreremin.predefense.registration.sys.util.Role;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateTeacherService {
	
	private final TeacherRepository teacherRepo;
	
	private final CreatePersonService createPersonService;
	
	@Transactional(isolation = Isolation.SERIALIZABLE,
            rollbackFor = { UniquenessViolationException.class })
	public void createTeacher(TeacherRequestDto dto) 
			throws UniquenessViolationException {
		
		long personId = createPersonService.createPerson(
				dto, Role.TEACHER.getRole()).getId();
		
		teacherRepo.save(new Teacher(personId, dto.getJobTitle()));
		log.info("The teacher created successfully");
	}
}
