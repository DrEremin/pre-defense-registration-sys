package ru.dreremin.predefense.registration.sys.services.registration;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import ru.dreremin.predefense.registration.sys.dto.request.AuthenticationDto;
import ru.dreremin.predefense.registration.sys.dto.response.ActualComissionForTeacherDto;
import ru.dreremin.predefense.registration.sys.dto.response.ActualCommissionForStudentDto;
import ru.dreremin.predefense.registration.sys.dto.response.CurrentCommissionOfStudentDto;
import ru.dreremin.predefense.registration.sys.exceptions
		 .FailedAuthenticationException;
import ru.dreremin.predefense.registration.sys.models.Commission;
import ru.dreremin.predefense.registration.sys.models.Note;
import ru.dreremin.predefense.registration.sys.models.Student;
import ru.dreremin.predefense.registration.sys.models.StudentCommission;
import ru.dreremin.predefense.registration.sys.models.StudentEntry;
import ru.dreremin.predefense.registration.sys.models.Teacher;
import ru.dreremin.predefense.registration.sys.models.TeacherEntry;
import ru.dreremin.predefense.registration.sys.repositories
		 .CommissionRepository;
import ru.dreremin.predefense.registration.sys.repositories.NoteRepository;
import ru.dreremin.predefense.registration.sys.repositories
		 .StudentCommissionRepository;
import ru.dreremin.predefense.registration.sys.repositories
		 .StudentEntryRepository;
import ru.dreremin.predefense.registration.sys.repositories.StudentRepository;
import ru.dreremin.predefense.registration.sys.repositories
		 .TeacherEntryRepository;
import ru.dreremin.predefense.registration.sys.repositories.TeacherRepository;
import ru.dreremin.predefense.registration.sys.security.ActorDetails;
import ru.dreremin.predefense.registration.sys.services.auth.AuthenticationService;

@RequiredArgsConstructor
@Service
public class ReadRegistrationService {

	private final StudentEntryRepository studentEntryRepo;
	
	private final TeacherEntryRepository teacherEntryRepo;
		
	private final StudentCommissionRepository studentCommissionRepo;
	
	private final CommissionRepository commissionRepo;
	
	private final NoteRepository noteRepo;
	
	private final StudentRepository studentRepo;
	
	private final TeacherRepository teacherRepo;
	
	@Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = {
			EntityNotFoundException.class })
	public CurrentCommissionOfStudentDto getCurrentComissionOfStudent() {
		
		Authentication authentication = SecurityContextHolder.getContext()
				.getAuthentication();
		ActorDetails actorDetails = (ActorDetails) authentication
				.getPrincipal();
		Optional<Student> studentOpt = studentRepo.findByActorLogin(
				actorDetails.getUsername());
		
		if (studentOpt.isEmpty()) {
			throw new EntityNotFoundException(
					"Student with this login does not exist");
		}
		
		Optional<StudentCommission> studentComissionOpt = 
				studentCommissionRepo.findByStudentId(studentOpt.get().getId());
		
		if (studentComissionOpt.isEmpty()) {
			throw new EntityNotFoundException("This student is not "
					+ "registered for any commission");
		}
		
		Commission commission = commissionRepo.findById(studentComissionOpt.get()
				.getCommissionId()).get();
		
		List<StudentEntry> students = studentEntryRepo.findAllByCommissionId(
				commission.getId(), Sort.by(Sort.Order.asc("p.lastName")));
		
		Collections.sort(students);
		return new CurrentCommissionOfStudentDto(commission, students);
	} 
	
	@Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = {
			EntityNotFoundException.class })
	public List<ActualCommissionForStudentDto> 
			getActualComissionsListForStudent() {
		
		Authentication authentication = SecurityContextHolder.getContext()
				.getAuthentication();
		ActorDetails actorDetails = (ActorDetails) authentication
				.getPrincipal();
		Optional<Student> studentOpt = studentRepo.findByActorLogin(
				actorDetails.getUsername());
		
		if (studentOpt.isEmpty()) {
			throw new EntityNotFoundException(
					"Student with this login does not exist");
		}
		
		List<Commission> actualComissions = commissionRepo
				.findAllByStartDateTimeGreaterThanOrderByStartDateTimeAsc(
						ZonedDateTime.now().plusHours(3))
				.stream()
				.filter(ac -> ac.getStudyDirection().equals(studentOpt.get()
						.getStudyDirection()))
				.collect(Collectors.toList());
		
		if (actualComissions.size() == 0) {
			throw new EntityNotFoundException("Аctual commissions not found");
		}

		List<ActualCommissionForStudentDto> resultDto = new ArrayList<>(
				actualComissions.size());
		
		for (Commission actualComission : actualComissions) {
			
			List<TeacherEntry> teachers = teacherEntryRepo
					.findAllByCommissionId(
							actualComission.getId(), 
							Sort.by(Sort.Order.asc("p.lastName")));
			Collections.sort(teachers);
			resultDto.add(new ActualCommissionForStudentDto(actualComission, 
					teachers));
		}
		return resultDto;
	}
	
	@Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = {
			EntityNotFoundException.class })
	public List<ActualComissionForTeacherDto> 
			getActualComissionsListForTeacher() {
		
		Authentication authentication = SecurityContextHolder.getContext()
				.getAuthentication();
		ActorDetails actorDetails = (ActorDetails) authentication
				.getPrincipal();
		Optional<Teacher> teacherOpt = teacherRepo.findByActorLogin(
				actorDetails.getUsername());
		
		if (teacherOpt.isEmpty()) {
			throw new EntityNotFoundException(
					"Teacher with this login does not exist");
		}
		
		List<Commission> actualCommissions = commissionRepo
				.findAllByStartDateTimeGreaterThanOrderByStartDateTimeAsc(
						ZonedDateTime.now());
		
		if (actualCommissions.size() == 0) {
			throw new EntityNotFoundException("Аctual commissions not found");
		}

		List<ActualComissionForTeacherDto> resultDto = new ArrayList<>(
				actualCommissions.size());
		
		for (Commission actualComission : actualCommissions) {
			
			List<TeacherEntry> teachers = teacherEntryRepo
					.findAllByCommissionId(
							actualComission.getId(), 
							Sort.by(Sort.Order.asc("p.lastName")));
			
			Collections.sort(teachers);
			Optional<Note> noteOpt = noteRepo.findByCommissionId(
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
