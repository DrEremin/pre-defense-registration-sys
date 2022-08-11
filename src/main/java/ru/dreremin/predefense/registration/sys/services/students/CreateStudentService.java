package ru.dreremin.predefense.registration.sys.services.students;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import ru.dreremin.predefense.registration.sys.dto.requestdto.impl.StudentDto;
import ru.dreremin.predefense.registration.sys.exceptions.UniquenessViolationException;
import ru.dreremin.predefense.registration.sys.factories.EntitiesFactory;
import ru.dreremin.predefense.registration.sys.repositories.StudentRepository;
import ru.dreremin.predefense.registration.sys.services.persons.CreatePersonService;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateStudentService {
	
	private final StudentRepository studentRepo;
	private final CreatePersonService createPersonService;
	
	@Transactional(isolation = Isolation.SERIALIZABLE,
            rollbackFor = { UniquenessViolationException.class })
	public void createStudent(StudentDto dto) 
			throws UniquenessViolationException{
		
		long personId = createPersonService.createPerson(dto).getId();
		
		studentRepo.save(EntitiesFactory.createStudent(dto, personId));
		log.info("The student created successfully");
	}
}
