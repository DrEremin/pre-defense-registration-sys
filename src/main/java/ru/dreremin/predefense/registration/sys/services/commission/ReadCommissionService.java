package ru.dreremin.predefense.registration.sys.services.commission;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.dreremin.predefense.registration.sys.dto.request
		 .TimePeriodRequestDto;
import ru.dreremin.predefense.registration.sys.dto.response
		 .CommissionResponseDto;
import ru.dreremin.predefense.registration.sys.dto.response
		 .CurrentCommissionResponseDto;
import ru.dreremin.predefense.registration.sys.dto.response
		 .WrapperForListResponseDto;
import ru.dreremin.predefense.registration.sys.dto.response
		 .WrapperForPageResponseDto;
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

@Slf4j
@RequiredArgsConstructor
@Service
public class ReadCommissionService {

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
	public CurrentCommissionResponseDto getCurrentComissionOfStudent() {
		
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
		return new CurrentCommissionResponseDto(commission, students);
	} 
	
	@Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = {
			EntityNotFoundException.class })
	public WrapperForPageResponseDto<Commission, CommissionResponseDto> 
			getActualComissionsListForStudent(PageRequest pageRequest) {
		
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
		
		Page<Commission> actualCommissions = commissionRepo
				.findAllActualCommissionsForStudent(
						ZonedDateTime.now().plusHours(3),
						studentOpt.get().getStudyDirection(),
						pageRequest);
		
		if (actualCommissions.getTotalElements() == 0) {
			throw new EntityNotFoundException("Аctual commissions not found");
		}
		return new WrapperForPageResponseDto<>(
				getResult(actualCommissions, false));
	}
	
	@Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = {
			EntityNotFoundException.class })
	public WrapperForPageResponseDto<Commission, CommissionResponseDto> 
			getActualComissionsListForTeacher(PageRequest pageRequest) {
		
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
		
		Page<Commission> actualCommissions = commissionRepo
				.findAllActualCommissionsForTeacher(
						ZonedDateTime.now(), 
						pageRequest);
		
		if (actualCommissions.getTotalElements() == 0) {
			throw new EntityNotFoundException("Аctual commissions not found");
		}
		return new WrapperForPageResponseDto<>(
				getResult(actualCommissions, true));
	}
	
	public WrapperForPageResponseDto<Commission, CommissionResponseDto> 
			getCommissionListByTimePeriod(
					TimePeriodRequestDto dto, 
					PageRequest pageRequest) {
		
		setZone(dto);
		
		Page<Commission> commissions = commissionRepo
				.findAllByStartDateTimeBetweenOrderByStartDateTime(
						dto.getStartDateTime(), 
						dto.getEndDateTime(),
						pageRequest);
		
		if (commissions.getTotalElements() == 0) {
			throw new EntityNotFoundException("Commissions not found");
		}
		return new WrapperForPageResponseDto<>(
				getResult(commissions, true));
	}
	
	public CommissionResponseDto getCommissionById(int id) {
		
		Optional<Commission> commissionOpt = commissionRepo.findById(id);
		
		if (commissionOpt.isEmpty()) {
			throw new EntityNotFoundException(
					"Commission with this id does not exist");
		}	
		return getResultDto(List.of(commissionOpt.get()), true).get(0);
	}
	
	private Map.Entry<Page<Commission>, List<CommissionResponseDto>> getResult(
			Page<Commission> page, 
			boolean isNote) {
		
		List<Commission> commissions = page.getContent();
		List<CommissionResponseDto> resultDto = new ArrayList<>(
				commissions.size());
		
		for (Commission commission : commissions) {
			
			List<TeacherEntry> teachers = teacherEntryRepo
					.findAllByCommissionId(
							commission.getId(), 
							Sort.by(Sort.Order.asc("p.lastName")));
			String note;
			
			if (isNote) {
				Optional<Note> noteOpt = noteRepo.findByCommissionId(
						commission.getId());
				note = noteOpt.isPresent() 
						? noteOpt.get().getNoteContent() : "";
			} else {
				note = null;
			}
			resultDto.add(new CommissionResponseDto(
					commission, 
					teachers, 
					note));
		}
		return Map.entry(page, resultDto);
	}
	
	private List<CommissionResponseDto> getResultDto(
			List<Commission> commissions, 
			boolean isNote) {
		
		
		
		List<CommissionResponseDto> resultDto = new ArrayList<>(
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
			
			resultDto.add(new CommissionResponseDto(
					commission, 
					teachers, 
					note));
		}
		return resultDto;
	}
	
	private void setZone(TimePeriodRequestDto dto) {
		
		dto.setStartDateTime(ZonedDateTime.of(
				dto.getStartDateTime().toLocalDateTime(), ZoneId.of(zone)));
		dto.setStartDateTime(ZonedDateTime.of(
				dto.getStartDateTime().toLocalDateTime(), ZoneId.of(zone)));
	}
}  
