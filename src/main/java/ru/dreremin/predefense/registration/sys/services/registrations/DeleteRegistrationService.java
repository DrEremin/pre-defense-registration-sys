package ru.dreremin.predefense.registration.sys.services.registrations;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

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

@Service
public class DeleteRegistrationService extends Registration {
	
	private Optional<StudentCommission> studentComissinOpt;
	
	private Optional<TeacherCommission> teacherComissinOpt;
	
	public DeleteRegistrationService(
			StudentCommissionRepository studentComissionRepo,
			TeacherCommissionRepository teacherComissionRepo,
			CommissionRepository comissionRepo,
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
            		EntityNotFoundException.class })
	public void deleteStudentRegistration() {
		
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
		setStudentComissionOpt();
		studentComissionRepo.delete(studentComissinOpt.get());
	}
	
	@Transactional(
			isolation = Isolation.SERIALIZABLE,
            rollbackFor = { 
            		EntityNotFoundException.class })
	public void deleteTeacherRegistration(int comissionId) {
		
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
		setTeacherComissionOpt();
		teacherComissionRepo.delete(teacherComissinOpt.get());
	}
	
	private void setStudentComissionOpt() {
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
