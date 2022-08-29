package ru.dreremin.predefense.registration.sys.services.authentication;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.dreremin.predefense.registration.sys.dto.requestdto.impl
		 .AuthenticationDto;
import ru.dreremin.predefense.registration.sys.exceptions
		 .FailedAuthenticationException;
import ru.dreremin.predefense.registration.sys.models.Administrator;
import ru.dreremin.predefense.registration.sys.models.Authentication;
import ru.dreremin.predefense.registration.sys.models.Student;
import ru.dreremin.predefense.registration.sys.models.Teacher;
import ru.dreremin.predefense.registration.sys.repositories
		 .AdministratorRepository;
import ru.dreremin.predefense.registration.sys.repositories
		 .AuthenticationRepository;
import ru.dreremin.predefense.registration.sys.repositories.StudentRepository;
import ru.dreremin.predefense.registration.sys.repositories.TeacherRepository;

@RequiredArgsConstructor
@Service
public class AuthenticationService {
	
	private final AuthenticationRepository authenticationRepo;
	
	private final AdministratorRepository administratorRepo;

	private final StudentRepository studentRepo;
	
	private final TeacherRepository teacherRepo;
	
	private Optional<Authentication> authenticationOpt;
	
	private Optional<Administrator> administratorOpt;
	
	private Optional<Student> studentOpt;
	
	private Optional<Teacher> teacherOpt;
	
	public Student studentAuthentication(AuthenticationDto dto) 
			throws EntityNotFoundException, FailedAuthenticationException {
		setAuthenticationOpt(dto);
		setStudentOpt();
		return studentOpt.get();
	}
	
	public Teacher teacherAuthentication(AuthenticationDto dto) 
			throws EntityNotFoundException, 
			FailedAuthenticationException {
		setAuthenticationOpt(dto);
		setTeacherOpt();
		return teacherOpt.get();
	}
	
	public Administrator administratorAuthentication(AuthenticationDto dto) 
			throws EntityNotFoundException, 
			FailedAuthenticationException {
		setAuthenticationOpt(dto);
		setAdministratorOpt();
		return administratorOpt.get();
	}
	
	private void setAuthenticationOpt(AuthenticationDto dto) 
			throws EntityNotFoundException, FailedAuthenticationException {
		authenticationOpt = authenticationRepo.findByLogin(
				dto.getPersonLogin());
		if (!authenticationOpt.isPresent()) {
			throw new EntityNotFoundException(
					"There is not exists person with this login");
		}
		if (!authenticationOpt.get().getPassword().equals(
				dto.getPersonPassword())) {
			throw new FailedAuthenticationException(
					"Сlient is not authenticated");
		}
	}
	
	private void setStudentOpt() 
			throws EntityNotFoundException {
		studentOpt = studentRepo.findByPersonId(
				authenticationOpt.get().getPersonId());
		if(!studentOpt.isPresent()) {
			throw new EntityNotFoundException(
					"There is not exists student with this login");
		}
	}
	
	private void setTeacherOpt() throws EntityNotFoundException {
		teacherOpt = teacherRepo.findByPersonId(
				authenticationOpt.get().getPersonId());
		if (!teacherOpt.isPresent()) {
			throw new EntityNotFoundException(
					"There is not exists teacher with this login");
		}
	}
	
	private void setAdministratorOpt() {
		administratorOpt = administratorRepo.findByPersonId(authenticationOpt
				.get().getPersonId());
		if (!administratorOpt.isPresent()) {
			throw new EntityNotFoundException(
					"There is not exists administrator with this login");
		}
	}
}
