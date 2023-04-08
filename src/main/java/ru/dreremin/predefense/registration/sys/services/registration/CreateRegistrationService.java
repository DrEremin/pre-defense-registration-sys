package ru.dreremin.predefense.registration.sys.services.registration;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import ru.dreremin.predefense.registration.sys.exceptions
		 .EntitiesMismatchException;
import ru.dreremin.predefense.registration.sys.exceptions.OverLimitException;
import ru.dreremin.predefense.registration.sys.exceptions.ExpiredCommissionException;
import ru.dreremin.predefense.registration.sys.exceptions
		 .UniquenessViolationException;
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

@Slf4j
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
	
	@Transactional(
			isolation = Isolation.SERIALIZABLE,
            rollbackFor = { 
            		EntityNotFoundException.class,
            		OverLimitException.class,
            		EntitiesMismatchException.class,
            		UniquenessViolationException.class,
            		ExpiredCommissionException.class})
	public void createStudentRegistration(int commissionId) {
		
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
		teacherCommissionRepo.save(new TeacherCommission(
				teacher.getId(), 
				comissionId));
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
		
		List<StudentCommission> registrations = studentCommissionRepo.findAll();
		
		if (registrations.size() >= commissionOpt.get().getStudentLimit()) {
			throw new OverLimitException(
					"The limit of the allowed number of students"
					+ " in this commission has been reached");
		}
		for (StudentCommission registration : registrations) {
			if (registration.getStudentId() == student.getId()) {
				throw new UniquenessViolationException(
						"Such a student is already"
						+ " registered for commission");
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
