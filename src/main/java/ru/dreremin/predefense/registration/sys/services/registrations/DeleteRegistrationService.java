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
import ru.dreremin.predefense.registration.sys.services.authentication.AuthenticationService;

@Service
public class DeleteRegistrationService extends Registration {
	
	private Optional<StudentComission> studentComissinOpt;
	
	private Optional<TeacherComission> teacherComissinOpt;
	
	public DeleteRegistrationService(
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
            		FailedAuthenticationException.class })
	public void deleteStudentRegistration(AuthorizationDto dto) 
			throws EntityNotFoundException, 
			FailedAuthenticationException {
		student = authenticationService.studentAuthentication(dto);
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
		teacher = authenticationService.teacherAuthentication(dto);
		setComissionOpt(dto.getComissionId());
		setTeacherComissionOpt();
		teacherComissionRepo.delete(teacherComissinOpt.get());
	}
	
	private void setStudentComissionOpt() throws EntityNotFoundException {
		studentComissinOpt = 
				studentComissionRepo.findByStudentId(student.getId());
		if (studentComissinOpt.isEmpty()) {
			throw new EntityNotFoundException(
					"The registration for this student does not exist");
		}
	}
	
	private void setTeacherComissionOpt() throws EntityNotFoundException {
		teacherComissinOpt = 
				teacherComissionRepo.findByTeacherIdAndComissionId(
						teacher.getId(), 
						comissionOpt.get().getId());
		if (teacherComissinOpt.isEmpty()) {
			throw new EntityNotFoundException(
					"The registration for this teacher "
					+ "for such comission does not exist");
		}
	}
}
