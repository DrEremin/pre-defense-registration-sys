package ru.dreremin.predefense.registration.sys.services.commission;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.dreremin.predefense.registration.sys.dto.response
		 .CommissionResponseDto;
import ru.dreremin.predefense.registration.sys.dto.response.StudentResponseDto;
import ru.dreremin.predefense.registration.sys.dto.response.TeacherResponseDto;
import ru.dreremin.predefense.registration.sys.dto.response
		 .WrapperForPageResponseDto;
import ru.dreremin.predefense.registration.sys.models.Actor;
import ru.dreremin.predefense.registration.sys.models.Commission;
import ru.dreremin.predefense.registration.sys.models.Note;
import ru.dreremin.predefense.registration.sys.models.Student;
import ru.dreremin.predefense.registration.sys.models.StudentCommission;
import ru.dreremin.predefense.registration.sys.models.Teacher;
import ru.dreremin.predefense.registration.sys.repositories
		 .CommissionRepository;
import ru.dreremin.predefense.registration.sys.repositories.NoteRepository;
import ru.dreremin.predefense.registration.sys.repositories
		 .StudentCommissionRepository;
import ru.dreremin.predefense.registration.sys.repositories.StudentRepository;
import ru.dreremin.predefense.registration.sys.repositories.TeacherRepository;
import ru.dreremin.predefense.registration.sys.security.ActorDetails;
import ru.dreremin.predefense.registration.sys.services.student
		 .ReadStudentService;
import ru.dreremin.predefense.registration.sys.services.teacher
		 .ReadTeacherService;
import ru.dreremin.predefense.registration.sys.util.ZonedDateTimeProvider;
import ru.dreremin.predefense.registration.sys.util.enums.Role;
@Slf4j
@RequiredArgsConstructor
@Service
public class ReadCommissionService {
		
	private final StudentCommissionRepository studentCommissionRepo;
	
	private final CommissionRepository commissionRepo;
	
	private final NoteRepository noteRepo;
	
	private final StudentRepository studentRepo;
	
	private final TeacherRepository teacherRepo;
	
	private final ZonedDateTimeProvider zonedDateTimeProvider;
	
	private final ReadTeacherService readTeacherService;
	
	private final ReadStudentService readStudentService;
	
	@Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = {
			EntityNotFoundException.class })
	public CommissionResponseDto getCurrentComissionOfStudent() {
		
		Optional<Student> studentOpt = studentRepo.findByActorLogin(
				authorityConfirmation(Role.STUDENT).getLogin());
		
		if (studentOpt.isEmpty()) {
			throw new EntityNotFoundException(
					"Student with this login does not exist");
		}
		log.debug("getCurrentComissionOfStudent()");
		Optional<StudentCommission> studentComissionOpt = 
				studentCommissionRepo.findByStudentIdAndActualTime(
						studentOpt.get().getId(), ZonedDateTime.now());
		
		if (studentComissionOpt.isEmpty()) {
			throw new EntityNotFoundException("This student is not "
					+ "registered for any actual commission");
		}
		
		Commission commission = commissionRepo.findById(studentComissionOpt
				.get().getCommissionId()).get();
		return getCommission(commission, true, true, true);
	} 
	
	@Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = {
			EntityNotFoundException.class, AccessDeniedException.class })
	public WrapperForPageResponseDto<Commission, CommissionResponseDto> 
			getActualComissionsListForStudent(PageRequest pageRequest) {
	
		Optional<Student> studentOpt = studentRepo.findByActorLogin(
				authorityConfirmation(Role.STUDENT).getLogin());
		
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
				getResult(actualCommissions));
	}
	
	@Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = {
			EntityNotFoundException.class, AccessDeniedException.class })
	public WrapperForPageResponseDto<Commission, CommissionResponseDto> 
			getActualComissionsListForTeacher(PageRequest pageRequest) {
		
		Optional<Teacher> teacherOpt = teacherRepo.findByActorLogin(
				authorityConfirmation(Role.TEACHER).getLogin());
		
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
				getResult(actualCommissions));
	}
	
	public WrapperForPageResponseDto<Commission, CommissionResponseDto> 
			getCommissionListByTimePeriod(
					ZonedDateTime startDateTime, 
					ZonedDateTime endDateTime,
					PageRequest pageRequest) {
		
		authorityConfirmation(Role.ADMIN);
		
		Page<Commission> commissions = commissionRepo
				.findAllByStartDateTimeBetweenOrderByStartDateTime(
						zonedDateTimeProvider
								.changeTimeZone(startDateTime), 
						zonedDateTimeProvider
								.changeTimeZone(endDateTime),
						pageRequest);
		
		if (commissions.getTotalElements() == 0) {
			throw new EntityNotFoundException("Commissions not found");
		}
		return new WrapperForPageResponseDto<>(
				getResult(commissions));
	}
	
	private CommissionResponseDto getCommission(
			Commission commission, 
			boolean withTeachers, 
			boolean withStudents,
			boolean withNote) {
			
		List<TeacherResponseDto> teachersDto = (withTeachers) 
				? getListOfTeacherResponseDto(commission.getId()) 
				: new ArrayList<>();
		List<StudentResponseDto> studentsDto = (withStudents) 
				? getListOfStudentResponseDto(commission.getId()) 
				: new ArrayList<>();
		String startDateTime = zonedDateTimeProvider.convertToString(
				commission.getStartDateTime());
		String endDateTime = zonedDateTimeProvider.convertToString(
				commission.getEndDateTime());
		String note = withNote 
				? noteRepo.findByCommissionId(commission.getId())
						.orElse(new Note(commission.getId(), ""))
						.getNoteContent() 
				: "";
		
		return new CommissionResponseDto(
				commission.getId(), 
				startDateTime, 
				endDateTime, 
				commission.getStudyDirection(), 
				commission.getPresenceFormat(), 
				commission.getLocation(), 
				commission.getStudentLimit(), 
				teachersDto,
				studentsDto,
				note);
	}
	
	public CommissionResponseDto getCommissionById(
			int id, 
			boolean withTeachers, 
			boolean withStudents,
			boolean withNote) {
		
		Actor actor = ((ActorDetails) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal()).getActor();
		
		if (!actor.getRole().equals(Role.ADMIN.getRole())) {
			throw new AccessDeniedException("User is not authorized");
		}
		
		Commission commission = commissionRepo.findById(id).orElseThrow(
				() -> new EntityNotFoundException(
						"Commission with this ID does not exist"));	
		return getCommission(
				commission, 
				withTeachers, 
				withStudents, 
				withNote);
	}
	
	private List<TeacherResponseDto> getListOfTeacherResponseDto(
			int commissionId) {
		
		List<Teacher> teachers = teacherRepo.findAllByCommissionId(
				commissionId, Sort.by(Sort.Order.asc("p.lastName")));
		List<TeacherResponseDto> teachersDto = new ArrayList<>(
				teachers.size());
		
		for (Teacher teacher : teachers) {
			teachersDto.add(readTeacherService.getTeacherResponseDto(teacher));
		}
		return teachersDto;
	}
	
	private List<StudentResponseDto> getListOfStudentResponseDto(
			int commissionId) {
		
		List<Student> students = studentRepo.findAllByCommissionId(
				commissionId, Sort.by(Sort.Order.asc("p.lastName")));
		List<StudentResponseDto> studentsDto = new ArrayList<>(
				students.size());
		
		for (Student student : students) {
			studentsDto.add(readStudentService.getStudentResponseDto(student));
		}
		return studentsDto;
	}
	
	private Map.Entry<Page<Commission>, List<CommissionResponseDto>> getResult(
			Page<Commission> page) {
		
		List<Commission> commissions = page.getContent();
		List<CommissionResponseDto> resultDto = new ArrayList<>(
				commissions.size());
		
		for (Commission commission : commissions) {
			
			resultDto.add(getCommission(
					commission, false, false, false));
		}
		return Map.entry(page, resultDto);
	}
	
	private Actor authorityConfirmation(Role role) {
		
		Actor actor = ((ActorDetails) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal()).getActor();
		
		if (!actor.getRole().equals(role.getRole())) {
			throw new AccessDeniedException("User is not 1 authorized");
		}
		return actor;
	}
}  
