package ru.dreremin.predefense.registration.sys.services.registration;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import ru.dreremin.predefense.registration.sys.exceptions
		 .EntitiesMismatchException;
import ru.dreremin.predefense.registration.sys.exceptions.OverLimitException;
import ru.dreremin.predefense.registration.sys.exceptions
		 .ExpiredCommissionException;
import ru.dreremin.predefense.registration.sys.exceptions
		 .UniquenessViolationException;
import ru.dreremin.predefense.registration.sys.models.Actor;
import ru.dreremin.predefense.registration.sys.models.Student;
import ru.dreremin.predefense.registration.sys.models.StudentCommission;
import ru.dreremin.predefense.registration.sys.models.Teacher;
import ru.dreremin.predefense.registration.sys.models.TeacherCommission;
import ru.dreremin.predefense.registration.sys.repositories
		 .CommissionRepository;
import ru.dreremin.predefense.registration.sys.repositories
		 .StudentCommissionRepository;
import ru.dreremin.predefense.registration.sys.repositories.StudentRepository;
import ru.dreremin.predefense.registration.sys.repositories
		 .TeacherCommissionRepository;
import ru.dreremin.predefense.registration.sys.repositories.TeacherRepository;
import ru.dreremin.predefense.registration.sys.security.ActorDetails;
import ru.dreremin.predefense.registration.sys.util.enums.Role;

@Service
public class CreateRegistrationService extends Registration {
	
	public CreateRegistrationService(
			StudentCommissionRepository studentCommissionRepo,
			TeacherCommissionRepository teacherCommissionRepo,
			CommissionRepository commissionRepo,
			StudentRepository studentRepo,
			TeacherRepository teacherRepo) {
		
		super( 
				studentCommissionRepo, 
				teacherCommissionRepo, 
				commissionRepo,
				studentRepo,
				teacherRepo);
	}
	
	public void createRegistration(int commissionId) {
		Actor actor = ((ActorDetails) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal()).getActor();
		if (actor.getRole().equals(Role.STUDENT.getRole())) {
			createStudentRegistration(commissionId, actor.getLogin());
		}
		if (actor.getRole().equals(Role.TEACHER.getRole())) {
			createTeacherRegistration(commissionId, actor.getLogin());
		}
	}
	
	@Transactional(
			isolation = Isolation.SERIALIZABLE,
            rollbackFor = { 
            		EntityNotFoundException.class,
            		OverLimitException.class,
            		EntitiesMismatchException.class,
            		UniquenessViolationException.class,
            		ExpiredCommissionException.class})
	private void createStudentRegistration(int commissionId, String login) {
		
		Optional<Student> studentOpt = studentRepo.findByActorLogin(login);
		
		if (studentOpt.isEmpty()) {
			throw new EntityNotFoundException(
					"Student with this login does not exist");
		}
		student = studentOpt.get();
		setComissionOpt(commissionId);
		checkingPossibilityOfStudentRegistration();
		studentCommissionRepo.save(new StudentCommission(
				student.getId(), commissionId));
	}
	
	@Transactional(
			isolation = Isolation.SERIALIZABLE,
            rollbackFor = { 
            		EntityNotFoundException.class,
            		UniquenessViolationException.class,
            		ExpiredCommissionException.class})
	private void createTeacherRegistration(int commissionId, String login) {
		
		Optional<Teacher> teacherOpt = teacherRepo.findByActorLogin(login);
		
		if (teacherOpt.isEmpty()) {
			throw new EntityNotFoundException(
					"Teacher with this login does not exist");
		}
		teacher = teacherOpt.get();
		setComissionOpt(commissionId);
		checkingPossibilityOfTeacherRegistration();
		teacherCommissionRepo.save(new TeacherCommission(
				teacher.getId(), 
				commissionId));
	}
	
	private void checkingPossibilityOfStudentRegistration() {
		if (!student.getStudyDirection().equals(
				commissionOpt.get().getStudyDirection())) {
			throw new EntitiesMismatchException(
					"The study direction of the commission and the"
					+ " student do not correspond to each other");
		}
		if (ZonedDateTime.now().plusHours(3).compareTo(
				commissionOpt.get().getStartDateTime()) > 0) {
			throw new ExpiredCommissionException(
					"The comission with such Id was expired");
		}
		
		List<StudentCommission> commissionRegistrations = studentCommissionRepo
				.findAllByCommissionId(commissionOpt.get().getId());
		
		if (commissionRegistrations.size() 
				>= commissionOpt.get().getStudentLimit()) {
			throw new OverLimitException(
					"The limit of the allowed number of students"
					+ " in this commission has been reached");
		}
		
		List<StudentCommission> actualRegistrations = studentCommissionRepo
				.findAllActualRegistrations(ZonedDateTime.now());
		
		for (StudentCommission registration : actualRegistrations) {
			if (registration.getStudentId() == student.getId()) {
				throw new UniquenessViolationException(
						"Such a student has already been signed up for "
						+ "this commission");
			}
		}
	}
	
	private void checkingPossibilityOfTeacherRegistration() {
		
		if (ZonedDateTime.now().compareTo(
				commissionOpt.get().getStartDateTime()) > 0) {
			throw new ExpiredCommissionException(
					"The comission with such Id was expired");
		}
		List<TeacherCommission> registrations = 
				teacherCommissionRepo.findAllByCommissionId(
						commissionOpt.get().getId());
		for (TeacherCommission registration : registrations) {
			if (registration.getTeacherId() == teacher.getId()) {
				throw new UniquenessViolationException(
						"Such a teacher is already"
						+ " registered for this commission");
			}
		}
	}
}
