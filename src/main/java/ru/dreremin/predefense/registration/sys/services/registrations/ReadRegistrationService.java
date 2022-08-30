package ru.dreremin.predefense.registration.sys.services.registrations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import ru.dreremin.predefense.registration.sys.dto.requestdto.impl
		 .AuthenticationDto;
import ru.dreremin.predefense.registration.sys.dto.responsedto.ActualComissionForStudentDto;
import ru.dreremin.predefense.registration.sys.dto.responsedto
		 .CurrentComissionOfStudentDto;
import ru.dreremin.predefense.registration.sys.exceptions
		 .FailedAuthenticationException;
import ru.dreremin.predefense.registration.sys.models.Comission;
import ru.dreremin.predefense.registration.sys.models.Student;
import ru.dreremin.predefense.registration.sys.models.StudentComission;
import ru.dreremin.predefense.registration.sys.models.StudentEntry;
import ru.dreremin.predefense.registration.sys.repositories
		 .ComissionRepository;
import ru.dreremin.predefense.registration.sys.repositories
		 .StudentComissionRepository;
import ru.dreremin.predefense.registration.sys.repositories
		 .StudentEntryRepository;
import ru.dreremin.predefense.registration.sys.services.authentication
		 .AuthenticationService;

@RequiredArgsConstructor
@Service
public class ReadRegistrationService {

	private final StudentEntryRepository studentEntryRepo;
	
	private final AuthenticationService authenticationService;
	
	private final StudentComissionRepository studentComissionRepo;
	
	private final ComissionRepository comissionRepo;
	
	private Optional<StudentComission> studentComissionOpt;
	
	private Comission comission;
	
	private Student student;
	
	@Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = {
			EntityNotFoundException.class, 
			FailedAuthenticationException.class})
	public CurrentComissionOfStudentDto getCurrentComissionOfStudent(
			AuthenticationDto dto) 
					throws EntityNotFoundException, 
					FailedAuthenticationException {
		
		student = authenticationService.studentAuthentication(dto);
		studentComissionOpt = 
				studentComissionRepo.findByStudentId(student.getId());
		if (studentComissionOpt.isEmpty()) {
			throw new EntityNotFoundException("This student is not "
					+ "registered for any commission");
		}
		comission = comissionRepo.findById(studentComissionOpt.get()
				.getComissionId()).get();
		
		List<StudentEntry> students = studentEntryRepo.findByComissionId(
				comission.getId(), Sort.by(Sort.Order.asc("p.lastName")));
		
		Collections.sort(students);
		return new CurrentComissionOfStudentDto(comission, students);
	} 
	
	public List<ActualComissionForStudentDto> getActualComissionsListForStudent(
			AuthenticationDto dto) throws Exception {
		
		student = authenticationService.studentAuthentication(dto);
		
		return null;
	}
}  
