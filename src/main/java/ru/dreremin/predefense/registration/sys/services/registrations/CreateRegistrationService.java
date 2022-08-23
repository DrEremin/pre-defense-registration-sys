package ru.dreremin.predefense.registration.sys.services.registrations;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ru.dreremin.predefense.registration.sys.dto.requestdto.impl
		 .RegistrationDto;
import ru.dreremin.predefense.registration.sys.exceptions
		 .EntitiesMismatchException;
import ru.dreremin.predefense.registration.sys.exceptions
		 .FailedAuthenticationException;
import ru.dreremin.predefense.registration.sys.exceptions.OverLimitException;
import ru.dreremin.predefense.registration.sys.exceptions
		 .UniquenessViolationException;
import ru.dreremin.predefense.registration.sys.models.Comission;
import ru.dreremin.predefense.registration.sys.models.Student;
import ru.dreremin.predefense.registration.sys.models.StudentComission;
import ru.dreremin.predefense.registration.sys.models.Teacher;
import ru.dreremin.predefense.registration.sys.models.TeacherComission;
import ru.dreremin.predefense.registration.sys.repositories
		 .ComissionRepository;
import ru.dreremin.predefense.registration.sys.repositories
		 .StudentComissionRepository;
import ru.dreremin.predefense.registration.sys.repositories
		 .TeacherComissionRepository;
import ru.dreremin.predefense.registration.sys.services.authentication
		 .AuthenticationService;

@Service
public class CreateRegistrationService extends Registration {
	
	public CreateRegistrationService(
			AuthenticationService authenticationService,
			StudentComissionRepository studentComissionRepo,
			TeacherComissionRepository teacherComissionRepo,
			ComissionRepository comissionRepo) {
		
		super(
				authenticationService, 
				studentComissionRepo, 
				teacherComissionRepo, 
				comissionRepo);
	}
	
	@Transactional(
			isolation = Isolation.SERIALIZABLE,
            rollbackFor = { 
            		EntityNotFoundException.class,
            		OverLimitException.class,
            		EntitiesMismatchException.class,
            		UniquenessViolationException.class,
            		FailedAuthenticationException.class })
	public void createStudentRegistration(RegistrationDto dto) 
			throws EntityNotFoundException, 
			OverLimitException, 
			EntitiesMismatchException,
			UniquenessViolationException,
			FailedAuthenticationException {
		
		student = authenticationService.studentAuthentication(dto);
		setComissionOpt(dto.getComissionId());
		checkingPossibilityOfStudentRegistration();
		studentComissionRepo.save(new StudentComission(
				student.getId(), 
				dto.getComissionId()));
	}
	
	@Transactional(
			isolation = Isolation.SERIALIZABLE,
            rollbackFor = { 
            		FailedAuthenticationException.class,
            		EntityNotFoundException.class,
            		UniquenessViolationException.class })
	public void createTeacherRegistration(RegistrationDto dto) 
			throws FailedAuthenticationException, 
			EntityNotFoundException, 
			UniquenessViolationException {
		teacher = authenticationService.teacherAuthentication(dto);
		setComissionOpt(dto.getComissionId());
		checkingPossibilityOfTeacherRegistration();
		teacherComissionRepo.save(new TeacherComission(
				teacher.getId(), 
				dto.getComissionId()));
	}
	
	private void checkingPossibilityOfStudentRegistration() 
			throws EntitiesMismatchException, 
			OverLimitException, 
			UniquenessViolationException {
		if (!student.getStudyDirection().equals(
				comissionOpt.get().getStudyDirection())) {
			throw new EntitiesMismatchException(
					"The study direction of the commission and the"
					+ " student do not correspond to each other");
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
	
	private void checkingPossibilityOfTeacherRegistration() 
			throws UniquenessViolationException {
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
