package ru.dreremin.predefense.registration.sys.services.registrations;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import ru.dreremin.predefense.registration.sys.models.Comission;
import ru.dreremin.predefense.registration.sys.models.Student;
import ru.dreremin.predefense.registration.sys.models.Teacher;
import ru.dreremin.predefense.registration.sys.repositories
		 .ComissionRepository;
import ru.dreremin.predefense.registration.sys.repositories
		 .StudentComissionRepository;
import ru.dreremin.predefense.registration.sys.repositories.StudentRepository;
import ru.dreremin.predefense.registration.sys.repositories
		 .TeacherComissionRepository;
import ru.dreremin.predefense.registration.sys.repositories.TeacherRepository;

@RequiredArgsConstructor
public abstract class Registration {
	
	//protected final AuthenticationService authenticationService;
	
	protected final StudentComissionRepository studentComissionRepo;
	
	protected final TeacherComissionRepository teacherComissionRepo;
	
	protected final ComissionRepository comissionRepo;
	
	protected final StudentRepository studentRepo;
	
	protected final TeacherRepository teacherRepo;
	
	protected Optional<Comission> comissionOpt;
	
	protected Student student;
	
	protected Teacher teacher;
	
	protected void setComissionOpt(int comissionId) 
			throws EntityNotFoundException {
		comissionOpt = comissionRepo.findById(comissionId);
		if(!comissionOpt.isPresent()) {
			throw new EntityNotFoundException(
					"There is not exists comission with this Id");
		}
	}
}
