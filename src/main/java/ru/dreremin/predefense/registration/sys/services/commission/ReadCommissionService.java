package ru.dreremin.predefense.registration.sys.services.commission;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
import ru.dreremin.predefense.registration.sys.models.Commission;
import ru.dreremin.predefense.registration.sys.models.Note;
import ru.dreremin.predefense.registration.sys.models.Student;
import ru.dreremin.predefense.registration.sys.models.Teacher;
import ru.dreremin.predefense.registration.sys.repositories
		 .CommissionRepository;
import ru.dreremin.predefense.registration.sys.repositories.NoteRepository;
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
	
	private final CommissionRepository commissionRepo;
	
	private final NoteRepository noteRepo;
	
	private final StudentRepository studentRepo;
	
	private final TeacherRepository teacherRepo;
	
	private final ZonedDateTimeProvider zonedDateTimeProvider;
	
	private final ReadTeacherService readTeacherService;
	
	private final ReadStudentService readStudentService;
	
	private ActorDetails getActorDetails() {
		return (ActorDetails) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
	}
	
	private CommissionResponseDto getCommission(
			Commission commission, 
			boolean withTeachers, 
			boolean withStudents,
			boolean withNote) {
			
		List<TeacherResponseDto> teachersDto = withTeachers
				? getListOfTeacherResponseDto(commission.getId()) 
				: new ArrayList<>();
		List<StudentResponseDto> studentsDto = withStudents 
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
	
	@Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = {
			EntityNotFoundException.class })
	private WrapperForPageResponseDto<Commission, CommissionResponseDto> 
			getActualCommissionsList(
					PageRequest pageRequest, 
					String studyDirection) {
		
		Page<Commission> actualCommissions = studyDirection == null 
				? commissionRepo.findAllCommissionsListAfter(
						ZonedDateTime.now(), 
						pageRequest) 
				: commissionRepo.findAllCommissionsListAfter(
						ZonedDateTime.now(),
						studyDirection,
						pageRequest);
		return new WrapperForPageResponseDto<>(
				getResult(actualCommissions));
	}
	
	private WrapperForPageResponseDto<Commission, CommissionResponseDto> 
			getCommissionsListByTimePeriod(
					String start, 
					String end,
					PageRequest pageRequest) {

		ZonedDateTime startDateTime, endDateTime;
		Page<Commission> commissions;
		
		try {
			startDateTime = zonedDateTimeProvider.parseFromString(start);
		} catch (DateTimeParseException e) {
			startDateTime = null;
		}
		try {
			endDateTime = zonedDateTimeProvider.parseFromString(end);
		} catch (DateTimeParseException e) {
			endDateTime = null;
		}
		if (startDateTime == null && endDateTime == null) {
			commissions = new PageImpl<>(new ArrayList<>(), pageRequest, 0);
			return new WrapperForPageResponseDto<>(getResult(commissions));
		} else if (startDateTime != null && endDateTime == null) {
			endDateTime = ZonedDateTime.now();
		} else if (startDateTime == null && endDateTime != null) {
			startDateTime = ZonedDateTime.of(
					1970, 1, 1, 0, 0, 0, 0, ZoneId.of("UTC+00:00"));
		}
		commissions = commissionRepo
				.findAllByStartDateTimeBetweenOrderByStartDateTime(
						zonedDateTimeProvider
								.changeTimeZone(startDateTime), 
						zonedDateTimeProvider
								.changeTimeZone(endDateTime),
						pageRequest);
		return new WrapperForPageResponseDto<>(
				getResult(commissions));
	}
	
	@Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = {
			EntityNotFoundException.class })
	private WrapperForPageResponseDto<Commission, CommissionResponseDto> 
			getMyCommissionsList(long id, PageRequest pageRequest) {
		
		Page<Commission> actualCommissions = commissionRepo
				.findAllByStudentIdAfter(
						id, 
						ZonedDateTime.now(), 
						pageRequest);
		
		return new WrapperForPageResponseDto<>(
				getResult(actualCommissions));
	}
	
	@Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = {
			EntityNotFoundException.class })
	private WrapperForPageResponseDto<Commission, CommissionResponseDto> 
			getMyCommissionsListForTeacher(int id, PageRequest pageRequest) {

			Page<Commission> actualCommissions = commissionRepo
					.findAllByTeacherIdAfter(
							id, 
							ZonedDateTime.now(), 
							pageRequest);
			return new WrapperForPageResponseDto<>(
					getResult(actualCommissions));
	}
	
	public WrapperForPageResponseDto<Commission, CommissionResponseDto> 
			getCommissionsList(
					PageRequest pageRequest, 
					String startDateTime, 
					String endDateTime,
					String my) {
		
		WrapperForPageResponseDto<Commission, CommissionResponseDto> 
				result = null;
		
		switch(Role.parseFromString(getActorDetails().getRole())) {
			case TEACHER:
		
				Teacher teacher = teacherRepo.findByActorLogin(
						getActorDetails().getUsername()).orElseThrow(
								() -> new EntityNotFoundException(
										"Teacher with this login does not "
										+ "exist"));
				log.debug(String.valueOf(teacher.getId()));
				result = my.length() == 0 
						? getActualCommissionsList(pageRequest, null) 
						: getMyCommissionsListForTeacher(teacher.getId(), pageRequest);
				break;
			case STUDENT:
				
				Student student = studentRepo.findByActorLogin(
						getActorDetails().getUsername()).orElseThrow(
								() -> new EntityNotFoundException(
										"Student with this login does not "
										+ "exist"));
				
				result = my.length() == 0 
						? getActualCommissionsList(
								pageRequest, 
								student.getStudyDirection()) 
						: getMyCommissionsList(student.getId(), pageRequest);
				break;
			case ADMIN:
				result = getCommissionsListByTimePeriod(
					startDateTime, 
					endDateTime, 
					pageRequest);
				break;
		}
		return result;
	} 
	
	public CommissionResponseDto getCommissionById(
			int id, 
			boolean withTeachers, 
			boolean withStudents,
			boolean withNote) {
		
		Commission commission = commissionRepo.findById(id).orElseThrow(
				() -> new EntityNotFoundException(
						"Commission with this ID does not exist"));	
		return getCommission(
				commission, 
				withTeachers, 
				withStudents, 
				withNote);
	}
}  
