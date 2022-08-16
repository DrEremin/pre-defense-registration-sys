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
import ru.dreremin.predefense.registration.sys.models.Authorization;
import ru.dreremin.predefense.registration.sys.models.Comission;
import ru.dreremin.predefense.registration.sys.models.Student;
import ru.dreremin.predefense.registration.sys.models.StudentComission;
import ru.dreremin.predefense.registration.sys.models.Teacher;
import ru.dreremin.predefense.registration.sys.models.TeacherComission;
import ru.dreremin.predefense.registration.sys.repositories
		 .AuthorizationRepository;
import ru.dreremin.predefense.registration.sys.repositories
		 .ComissionRepository;
import ru.dreremin.predefense.registration.sys.repositories
		 .StudentComissionRepository;
import ru.dreremin.predefense.registration.sys.repositories.StudentRepository;
import ru.dreremin.predefense.registration.sys.repositories
		 .TeacherComissionRepository;
import ru.dreremin.predefense.registration.sys.repositories.TeacherRepository;

@Service
@RequiredArgsConstructor
public class CreateRegistrationService {
	
	private final StudentComissionRepository studentComissionRepo;
	
	private final TeacherComissionRepository teacherComissionRepo;
	
	private final AuthorizationRepository authorizationRepo;
	
	private final StudentRepository studentRepo;
	
	private final TeacherRepository teacherRepo;
	
	private final ComissionRepository comissionRepo;
	
	private Optional<Authorization> authorizationOpt;
	
	private Optional<Student> studentOpt;
	
	private Optional<Teacher> teacherOpt;
	
	private Optional<Comission> comissionOpt;
	
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
		
		setAuthorizationOpt(dto);
		setComissionOpt(dto.getComissionId());
		setStudentOpt();
		checkingPossibilityOfStudentRegistration();
		studentComissionRepo.save(new StudentComission(
				studentOpt.get().getId(), 
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
		setAuthorizationOpt(dto);
		setComissionOpt(dto.getComissionId());
		setTeacherOpt();
		checkingPossibilityOfTeacherRegistration();
		teacherComissionRepo.save(new TeacherComission(
				teacherOpt.get().getId(), 
				dto.getComissionId()));
	}
	
	private void setAuthorizationOpt(RegistrationDto dto ) 
			throws EntityNotFoundException, FailedAuthenticationException {
		authorizationOpt = authorizationRepo.findByLogin(dto.getPersonLogin());
		if (!authorizationOpt.isPresent()) {
			throw new EntityNotFoundException(
					"There is not exists person with this login");
		}
		if (!authorizationOpt.get().getPassword().equals(
				dto.getPersonPassword())) {
			throw new FailedAuthenticationException(
					"Сlient is not authenticated");
		}
	}
	
	private void setComissionOpt(int comissionId) 
			throws EntityNotFoundException {
		comissionOpt = comissionRepo.findById(comissionId);
		if(!comissionOpt.isPresent()) {
			throw new EntityNotFoundException(
					"There is not exists comission with this Id");
		}
	}
	
	private void setStudentOpt() 
			throws EntityNotFoundException, EntitiesMismatchException {
		studentOpt = studentRepo.findByPersonId(
				authorizationOpt.get().getPersonId());
		if(!studentOpt.isPresent()) {
			throw new EntityNotFoundException(
					"There is not exists student with this login");
		}
	}
	
	private void setTeacherOpt() throws EntityNotFoundException {
		teacherOpt = teacherRepo.findByPersonId(
				authorizationOpt.get().getPersonId());
		if (!teacherOpt.isPresent()) {
			throw new EntityNotFoundException(
					"There is not exists teacher with this login");
		}
	}
	
	private void checkingPossibilityOfStudentRegistration() 
			throws EntitiesMismatchException, 
			OverLimitException, 
			UniquenessViolationException {
		if (!studentOpt.get().getStudyDirection().equals(
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
			if (registration.getStudentId() == studentOpt.get().getId()) {
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
			if (registration.getTeacherId() == teacherOpt.get().getId()) {
				throw new UniquenessViolationException(
						"Such a teacher is already"
						+ " registered for this commission");
			}
		}
	}
}
