package ru.dreremin.predefense.registration.sys.services.registrations;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import ru.dreremin.predefense.registration.sys.dto.requestdto.AuthenticationDto;
import ru.dreremin.predefense.registration.sys.dto.responsedto
		 .ActualComissionForStudentDto;
import ru.dreremin.predefense.registration.sys.dto.responsedto
		 .ActualComissionForTeacherDto;
import ru.dreremin.predefense.registration.sys.dto.responsedto
		 .CurrentComissionOfStudentDto;
import ru.dreremin.predefense.registration.sys.exceptions
		 .FailedAuthenticationException;
import ru.dreremin.predefense.registration.sys.models.Comission;
import ru.dreremin.predefense.registration.sys.models.Note;
import ru.dreremin.predefense.registration.sys.models.Student;
import ru.dreremin.predefense.registration.sys.models.StudentComission;
import ru.dreremin.predefense.registration.sys.models.StudentEntry;
import ru.dreremin.predefense.registration.sys.models.TeacherEntry;
import ru.dreremin.predefense.registration.sys.repositories
		 .ComissionRepository;
import ru.dreremin.predefense.registration.sys.repositories.NoteRepository;
import ru.dreremin.predefense.registration.sys.repositories
		 .StudentComissionRepository;
import ru.dreremin.predefense.registration.sys.repositories
		 .StudentEntryRepository;
import ru.dreremin.predefense.registration.sys.repositories
		 .TeacherEntryRepository;
import ru.dreremin.predefense.registration.sys.services.authentication
		 .AuthenticationService;

@RequiredArgsConstructor
@Service
public class ReadRegistrationService {

	private final StudentEntryRepository studentEntryRepo;
	
	private final TeacherEntryRepository teacherEntryRepo;
	
	private final AuthenticationService authenticationService;
	
	private final StudentComissionRepository studentComissionRepo;
	
	private final ComissionRepository comissionRepo;
	
	private final NoteRepository noteRepo;
	
	private Student student;
	
	@Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = {
			EntityNotFoundException.class, 
			FailedAuthenticationException.class})
	public CurrentComissionOfStudentDto getCurrentComissionOfStudent(
			AuthenticationDto dto) 
					throws EntityNotFoundException, 
					FailedAuthenticationException {
		
		student = authenticationService.studentAuthentication(dto);
		
		Optional<StudentComission> studentComissionOpt = 
				studentComissionRepo.findByStudentId(student.getId());
		
		if (studentComissionOpt.isEmpty()) {
			throw new EntityNotFoundException("This student is not "
					+ "registered for any commission");
		}
		
		Comission comission = comissionRepo.findById(studentComissionOpt.get()
				.getComissionId()).get();
		
		List<StudentEntry> students = studentEntryRepo.findByComissionId(
				comission.getId(), Sort.by(Sort.Order.asc("p.lastName")));
		
		Collections.sort(students);
		return new CurrentComissionOfStudentDto(comission, students);
	} 
	
	@Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = {
			EntityNotFoundException.class, 
			FailedAuthenticationException.class})
	public List<ActualComissionForStudentDto> getActualComissionsListForStudent(
			AuthenticationDto dto) 
					throws EntityNotFoundException, 
					FailedAuthenticationException  {
		
		student = authenticationService.studentAuthentication(dto);
		
		List<Comission> actualComissions = comissionRepo
				.findByStartDateTimeGreaterThanOrderByStartDateTimeAsc(
						ZonedDateTime.now().plusHours(3))
				.stream()
				.filter(ac -> ac.getStudyDirection().equals(student
						.getStudyDirection()))
				.collect(Collectors.toList());
		
		if (actualComissions.size() == 0) {
			throw new EntityNotFoundException("Аctual commissions not found");
		}

		List<ActualComissionForStudentDto> resultDto = new ArrayList<>(
				actualComissions.size());
		
		for (Comission actualComission : actualComissions) {
			
			List<TeacherEntry> teachers = teacherEntryRepo.findByComissionId(
					actualComission.getId(), 
					Sort.by(Sort.Order.asc("p.lastName")));
			Collections.sort(teachers);
			resultDto.add(new ActualComissionForStudentDto(actualComission, 
					teachers));
		}
		return resultDto;
	}
	
	@Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = {
			EntityNotFoundException.class, 
			FailedAuthenticationException.class})
	public List<ActualComissionForTeacherDto> getActualComissionsListForTeacher(
			AuthenticationDto dto) 
					throws EntityNotFoundException, 
					FailedAuthenticationException  {
		
		authenticationService.teacherAuthentication(dto);
		
		List<Comission> actualComissions = comissionRepo
				.findByStartDateTimeGreaterThanOrderByStartDateTimeAsc(
						ZonedDateTime.now());
		
		if (actualComissions.size() == 0) {
			throw new EntityNotFoundException("Аctual commissions not found");
		}

		List<ActualComissionForTeacherDto> resultDto = new ArrayList<>(
				actualComissions.size());
		
		for (Comission actualComission : actualComissions) {
			
			List<TeacherEntry> teachers = teacherEntryRepo.findByComissionId(
					actualComission.getId(), 
					Sort.by(Sort.Order.asc("p.lastName")));
			
			Collections.sort(teachers);
			
			Optional<Note> noteOpt = noteRepo.findByComissionId(
					actualComission.getId());
			String note = noteOpt.isPresent() 
					? noteOpt.get().getNoteContent() : "";
			
			resultDto.add(new ActualComissionForTeacherDto(
					actualComission, 
					teachers, 
					note));
		}
		return resultDto;
	}
}  
