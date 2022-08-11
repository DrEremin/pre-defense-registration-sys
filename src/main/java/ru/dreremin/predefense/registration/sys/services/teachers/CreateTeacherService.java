package ru.dreremin.predefense.registration.sys.services.teachers;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.dreremin.predefense.registration.sys.dto.requestdto.impl.TeacherDto;
import ru.dreremin.predefense.registration.sys.exceptions
		 .UniquenessViolationException;
import ru.dreremin.predefense.registration.sys.models.Teacher;
import ru.dreremin.predefense.registration.sys.repositories.TeacherRepository;
import ru.dreremin.predefense.registration.sys.services.persons
		 .CreatePersonService;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateTeacherService {
	
	private final TeacherRepository teacherRepo;
	private final CreatePersonService createPersonService;
	
	@Transactional(isolation = Isolation.SERIALIZABLE,
            rollbackFor = { UniquenessViolationException.class })
	public void createTeacher(TeacherDto dto) 
			throws UniquenessViolationException{
		
		long personId = createPersonService.createPerson(dto).getId();
		
		teacherRepo.save(new Teacher(personId, dto.getJobTitle()));
		log.info("The teacher created successfully");
	}
}
