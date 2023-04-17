package ru.dreremin.predefense.registration.sys.services.registration;

import java.time.ZonedDateTime;
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
	
	private Optional<StudentCommission> studentCommissinOpt;
	
	private Optional<TeacherCommission> teacherCommissinOpt;
	
	public DeleteRegistrationService(
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
		studentCommissionRepo.delete(studentCommissinOpt.get());
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
		teacherCommissionRepo.delete(teacherCommissinOpt.get());
	}
	
	private void setStudentComissionOpt() {
		studentCommissinOpt = 
				studentCommissionRepo.findByStudentIdAndActualTime(
						student.getId(), 
						ZonedDateTime.now());
		if (studentCommissinOpt.isEmpty()) {
			throw new EntityNotFoundException(
					"The actual registration for this student does not exist");
		}
	}
	
	private void setTeacherComissionOpt() {
		teacherCommissinOpt = 
				teacherCommissionRepo.findByTeacherIdAndCommissionId(
						teacher.getId(), 
						commissionOpt.get().getId());
		if (teacherCommissinOpt.isEmpty()) {
			throw new EntityNotFoundException(
					"The registration for this teacher "
					+ "for such comission does not exist");
		}
	}
}
