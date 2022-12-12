package ru.dreremin.predefense.registration.sys.services.registrations;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import ru.dreremin.predefense.registration.sys.exceptions
		 .EntitiesMismatchException;
import ru.dreremin.predefense.registration.sys.exceptions.OverLimitException;
import ru.dreremin.predefense.registration.sys.exceptions.ExpiredComissionException;
import ru.dreremin.predefense.registration.sys.exceptions
		 .UniquenessViolationException;
import ru.dreremin.predefense.registration.sys.models.Student;
import ru.dreremin.predefense.registration.sys.models.StudentComission;
import ru.dreremin.predefense.registration.sys.models.Teacher;
import ru.dreremin.predefense.registration.sys.models.TeacherComission;
import ru.dreremin.predefense.registration.sys.repositories
		 .ComissionRepository;
import ru.dreremin.predefense.registration.sys.repositories
		 .StudentComissionRepository;
import ru.dreremin.predefense.registration.sys.repositories.StudentRepository;
import ru.dreremin.predefense.registration.sys.repositories
		 .TeacherComissionRepository;
import ru.dreremin.predefense.registration.sys.repositories.TeacherRepository;
import ru.dreremin.predefense.registration.sys.security.ActorDetails;
import ru.dreremin.predefense.registration.sys.services.authentication
		 .AuthenticationService;

@Service
public class CreateRegistrationService extends Registration {
	
	public CreateRegistrationService(
			StudentComissionRepository studentComissionRepo,
			TeacherComissionRepository teacherComissionRepo,
			ComissionRepository comissionRepo,
			StudentRepository studentRepo,
			TeacherRepository teacherRepo) {
		
		super( 
				studentComissionRepo, 
				teacherComissionRepo, 
				comissionRepo,
				studentRepo,
				teacherRepo);
	}
	
	@Transactional(
			isolation = Isolation.SERIALIZABLE,
            rollbackFor = { 
            		EntityNotFoundException.class,
            		OverLimitException.class,
            		EntitiesMismatchException.class,
            		UniquenessViolationException.class,
            		ExpiredComissionException.class})
	public void createStudentRegistration(int comissionId) {
		
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
		student = studentOpt.get();
		setComissionOpt(comissionId);
		checkingPossibilityOfStudentRegistration();
		studentComissionRepo.save(new StudentComission(
				student.getId(), comissionId));
	}
	
	@Transactional(
			isolation = Isolation.SERIALIZABLE,
            rollbackFor = { 
            		EntityNotFoundException.class,
            		UniquenessViolationException.class,
            		ExpiredComissionException.class})
	public void createTeacherRegistration(int comissionId) {
		
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
		teacher = teacherOpt.get();
		setComissionOpt(comissionId);
		checkingPossibilityOfTeacherRegistration();
		teacherComissionRepo.save(new TeacherComission(
				teacher.getId(), 
				comissionId));
	}
	
	private void checkingPossibilityOfStudentRegistration() {
		if (!student.getStudyDirection().equals(
				comissionOpt.get().getStudyDirection())) {
			throw new EntitiesMismatchException(
					"The study direction of the commission and the"
					+ " student do not correspond to each other");
		}
		if (ZonedDateTime.now().plusHours(3).compareTo(
				comissionOpt.get().getStartDateTime()) > 0) {
			throw new ExpiredComissionException(
					"The comission with such Id was expired");
		}
		
		List<StudentComission> registrations = 
				studentComissionRepo.findByComissionId(
						comissionOpt.get().getId());
		
		if (registrations.size() >= comissionOpt.get().getStudentLimit()) {
			throw new OverLimitException(
					"The limit of the allowed number of students"
					+ " in this commission has been reached");
		}
		for (StudentComission registration : registrations) {
			if (registration.getStudentId() == student.getId()) {
				throw new UniquenessViolationException(
						"Such a student is already"
						+ " registered for this commission");
			}
		}
	}
	
	private void checkingPossibilityOfTeacherRegistration() {
		
		if (ZonedDateTime.now().compareTo(
				comissionOpt.get().getStartDateTime()) > 0) {
			throw new ExpiredComissionException(
					"The comission with such Id was expired");
		}
		List<TeacherComission> registrations = 
				teacherComissionRepo.findByComissionId(
						comissionOpt.get().getId());
		for (TeacherComission registration : registrations) {
			if (registration.getTeacherId() == teacher.getId()) {
				throw new UniquenessViolationException(
						"Such a teacher is already"
						+ " registered for this commission");
			}
		}
	}
}
