package ru.dreremin.predefense.registration.sys.services.student;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import ru.dreremin.predefense.registration.sys.dto.request.StudentRequestDto;
import ru.dreremin.predefense.registration.sys.exceptions
		 .UniquenessViolationException;
import ru.dreremin.predefense.registration.sys.repositories.StudentRepository;
import ru.dreremin.predefense.registration.sys.services.person
		 .CreatePersonService;
import ru.dreremin.predefense.registration.sys.util.EntitiesFactory;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateStudentService {
	
	private final StudentRepository studentRepo;
	
	private final CreatePersonService createPersonService;
	
	@Transactional(isolation = Isolation.SERIALIZABLE,
            rollbackFor = { UniquenessViolationException.class })
	public void createStudent(StudentRequestDto dto) 
			throws UniquenessViolationException{
		
		long personId = createPersonService.createPerson(dto, "ROLE_STUDENT")
				.getId();
		
		studentRepo.save(EntitiesFactory.createStudent(dto, personId));
		log.info("The student created successfully");
	}
}
