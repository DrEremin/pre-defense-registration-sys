package ru.dreremin.predefense.registration.sys.services.registrations;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import ru.dreremin.predefense.registration.sys.dto.requestdto.impl
		 .AuthorizationDto;
import ru.dreremin.predefense.registration.sys.dto.requestdto.impl
		 .RegistrationDto;
import ru.dreremin.predefense.registration.sys.exceptions.EntitiesMismatchException;
import ru.dreremin.predefense.registration.sys.exceptions
		 .FailedAuthenticationException;
import ru.dreremin.predefense.registration.sys.exceptions.OverLimitException;
import ru.dreremin.predefense.registration.sys.exceptions.UniquenessViolationException;
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

@RequiredArgsConstructor
@Service
public class DeleteRegistrationService {

	private final StudentComissionRepository studentComissionRepo;
	
	private final TeacherComissionRepository teacherComissionRepo;
	
	private final AuthorizationRepository authorizationRepo;
	
	private final StudentRepository studentRepo;
	
	private final TeacherRepository teacherRepo;
	
	private final ComissionRepository comissionRepo;
	
	private Optional<Authorization> authorizationOpt;
	
	private Optional<Student> studentOpt;
	
	private Optional<Teacher> teacherOpt;
	
	private Optional<StudentComission> studentComissinOpt;
	
	private Optional<TeacherComission> teacherComissinOpt;
	
	private Optional<Comission> comissionOpt;
	
	@Transactional(
			isolation = Isolation.SERIALIZABLE,
            rollbackFor = { 
            		EntityNotFoundException.class,
            		FailedAuthenticationException.class })
	public void deleteStudentRegistration(AuthorizationDto dto) 
			throws EntityNotFoundException, 
			FailedAuthenticationException {
		setAuthorizationOpt(dto);
		setStudentOpt();
		setStudentComissionOpt();
		studentComissionRepo.delete(studentComissinOpt.get());
	}
	
	@Transactional(
			isolation = Isolation.SERIALIZABLE,
            rollbackFor = { 
            		EntityNotFoundException.class,
            		FailedAuthenticationException.class })
	public void deleteTeacherRegistration(RegistrationDto dto) 
			throws EntityNotFoundException, 
			FailedAuthenticationException {
		setAuthorizationOpt(dto);
		setTeacherOpt();
		setComissionOpt(dto.getComissionId());
		setTeacherComissionOpt();
		teacherComissionRepo.delete(teacherComissinOpt.get());
	}
	
	private void setAuthorizationOpt(AuthorizationDto dto) 
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
	
	private void setStudentOpt() 
			throws EntityNotFoundException {
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
	
	private void setStudentComissionOpt() throws EntityNotFoundException {
		studentComissinOpt = 
				studentComissionRepo.findByStudentId(studentOpt.get().getId());
		if (studentComissinOpt.isEmpty()) {
			throw new EntityNotFoundException(
					"The registration for this student does not exist");
		}
	}
	
	private void setTeacherComissionOpt() throws EntityNotFoundException {
		teacherComissinOpt = 
				teacherComissionRepo.findByTeacherIdAndComissionId(
						teacherOpt.get().getId(), 
						comissionOpt.get().getId());
		if (teacherComissinOpt.isEmpty()) {
			throw new EntityNotFoundException(
					"The registration for this teacher "
					+ "for such comission does not exist");
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
}
