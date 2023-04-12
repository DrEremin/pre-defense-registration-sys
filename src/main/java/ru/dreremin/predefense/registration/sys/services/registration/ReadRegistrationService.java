package ru.dreremin.predefense.registration.sys.services.registration;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import ru.dreremin.predefense.registration.sys.dto.request.TimePeriodDto;
import ru.dreremin.predefense.registration.sys.dto.response.CommissionDto;
import ru.dreremin.predefense.registration.sys.dto.response
		 .CurrentCommissionOfStudentDto;
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
	
	@Value("${spring.zone}")
	private String zone;
	
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
				studentCommissionRepo.findByStudentIdAndActualTime(
						studentOpt.get().getId(), ZonedDateTime.now());
		
		if (studentComissionOpt.isEmpty()) {
			throw new EntityNotFoundException("This student is not "
					+ "registered for any actual commission");
		}
		
		Commission commission = commissionRepo.findById(studentComissionOpt
				.get().getCommissionId()).get();
		
		List<StudentEntry> students = studentEntryRepo.findAllByCommissionId(
				commission.getId(), Sort.by(Sort.Order.asc("p.lastName")));
		
		Collections.sort(students);
		return new CurrentCommissionOfStudentDto(commission, students);
	} 
	
	@Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = {
			EntityNotFoundException.class })
	public List<CommissionDto> 
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
		
		List<Commission> actualCommissions = commissionRepo
				.findAllByStartDateTimeGreaterThanOrderByStartDateTimeAsc(
						ZonedDateTime.now().plusHours(3))
				.stream()
				.filter(ac -> ac.getStudyDirection().equals(studentOpt.get()
						.getStudyDirection()))
				.collect(Collectors.toList());
		
		if (actualCommissions.size() == 0) {
			throw new EntityNotFoundException("Аctual commissions not found");
		}
		
		return getResultDto(actualCommissions, false);
	}
	
	@Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = {
			EntityNotFoundException.class })
	public List<CommissionDto> 
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
		return getResultDto(actualCommissions, true);
	}
	
	public List<CommissionDto> getCommissionListByTimePeriod(
			TimePeriodDto dto) {
		
		setZone(dto);
		
		List<Commission> commissions = commissionRepo
				.findAllByStartDateTimeBetweenOrderByStartDateTime(
						dto.getStartDateTime(), 
						dto.getEndDateTime());
		
			
		if (commissions.size() == 0) {
			throw new EntityNotFoundException("Commissions not found");
		}
		return getResultDto(commissions, true);	
	}
	
	public List<CommissionDto> getCommissionById(int id) {
		
		Optional<Commission> commissionOpt = commissionRepo.findById(id);
		
		if (commissionOpt.isEmpty()) {
			throw new EntityNotFoundException(
					"Commission with this id does not exist");
		}	
		return getResultDto(List.of(commissionOpt.get()), true);
	}
	
	private List<CommissionDto> getResultDto(
			List<Commission> commissions, 
			boolean isNote) {
		
		
		
		List<CommissionDto> resultDto = new ArrayList<>(
				commissions.size());
		
		for (Commission commission : commissions) {
			
			List<TeacherEntry> teachers = teacherEntryRepo
					.findAllByCommissionId(
							commission.getId(), 
							Sort.by(Sort.Order.asc("p.lastName")));
			String note;
			
			Collections.sort(teachers);
			if (isNote) {
				Optional<Note> noteOpt = noteRepo.findByCommissionId(
						commission.getId());
				note = noteOpt.isPresent() 
						? noteOpt.get().getNoteContent() : "";
			} else {
				note = null;
			}
			
			resultDto.add(new CommissionDto(
					commission, 
					teachers, 
					note));
		}
		return resultDto;
	}
	
	private void setZone(TimePeriodDto dto) {
		
		dto.setStartDateTime(ZonedDateTime.of(
				dto.getStartDateTime().toLocalDateTime(), ZoneId.of(zone)));
		dto.setStartDateTime(ZonedDateTime.of(
				dto.getStartDateTime().toLocalDateTime(), ZoneId.of(zone)));
	}
}  
