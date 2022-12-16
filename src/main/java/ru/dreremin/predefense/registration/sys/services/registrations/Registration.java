package ru.dreremin.predefense.registration.sys.services.registrations;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import ru.dreremin.predefense.registration.sys.models.Commission;
import ru.dreremin.predefense.registration.sys.models.Student;
import ru.dreremin.predefense.registration.sys.models.Teacher;
import ru.dreremin.predefense.registration.sys.repositories
		 .CommissionRepository;
import ru.dreremin.predefense.registration.sys.repositories
		 .StudentCommissionRepository;
import ru.dreremin.predefense.registration.sys.repositories.StudentRepository;
import ru.dreremin.predefense.registration.sys.repositories
		 .TeacherCommissionRepository;
import ru.dreremin.predefense.registration.sys.repositories.TeacherRepository;

@RequiredArgsConstructor
public abstract class Registration {
	
	protected final StudentCommissionRepository studentCommissionRepo;
	
	protected final TeacherCommissionRepository teacherCommissionRepo;
	
	protected final CommissionRepository commissionRepo;
	
	protected final StudentRepository studentRepo;
	
	protected final TeacherRepository teacherRepo;
	
	protected Optional<Commission> commissionOpt;
	
	protected Student student;
	
	protected Teacher teacher;
	
	protected void setComissionOpt(int comissionId) 
			throws EntityNotFoundException {
		commissionOpt = commissionRepo.findById(comissionId);
		if(!commissionOpt.isPresent()) {
			throw new EntityNotFoundException(
					"There is not exists comission with this Id");
		}
	}
}
