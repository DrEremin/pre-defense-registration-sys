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
import ru.dreremin.predefense.registration.sys.exceptions.OverLimitException;
import ru.dreremin.predefense.registration.sys.exceptions
		 .UniquenessViolationException;
import ru.dreremin.predefense.registration.sys.models.Authorization;
import ru.dreremin.predefense.registration.sys.models.Comission;
import ru.dreremin.predefense.registration.sys.models.Student;
import ru.dreremin.predefense.registration.sys.models.StudentComission;
import ru.dreremin.predefense.registration.sys.repositories
		 .AuthorizationRepository;
import ru.dreremin.predefense.registration.sys.repositories
		 .ComissionRepository;
import ru.dreremin.predefense.registration.sys.repositories
		 .StudentComissionRepository;
import ru.dreremin.predefense.registration.sys.repositories.StudentRepository;

@Service
@RequiredArgsConstructor
public class CreateRegistrationService {
	
	private final StudentComissionRepository studentComissionRepo;
	
	private final AuthorizationRepository authorizationRepo;
	
	private final StudentRepository studentRepo;
	
	private final ComissionRepository comissionRepo;
	
	private Optional<Authorization> authorization;
	
	private Optional<Student> student;
	
	private Optional<Comission> comission;
	
	@Transactional(
			isolation = Isolation.SERIALIZABLE,
            rollbackFor = { 
            		EntityNotFoundException.class,
            		OverLimitException.class,
            		EntitiesMismatchException.class,
            		UniquenessViolationException.class })
	public void studentRegistration(RegistrationDto dto) throws
			EntityNotFoundException, 
			OverLimitException, 
			EntitiesMismatchException,
			UniquenessViolationException {
		
		authorization = authorizationRepo.findByLogin(dto.getPersonLogin());
		if (!authorization.isPresent()) {
			throw new EntityNotFoundException(
					"There is not exists person with this login");
		}
		student = studentRepo.findByPersonId(authorization.get().getPersonId());
		if(!student.isPresent()) {
			throw new EntityNotFoundException(
					"There is not exists student with this login");
		}
		comission = comissionRepo.findById(dto.getComissionId());
		if(!comission.isPresent()) {
			throw new EntityNotFoundException(
					"There is not exists comission with this Id");
		}
		if (!student.get().getStudyDirection().equals(
				comission.get().getStudyDirection())) {
			throw new EntitiesMismatchException(
					"The study direction of the commission and the"
					+ " student do not correspond to each other");
		}
		
		List<StudentComission> registrations = 
				studentComissionRepo.findByComissionId(dto.getComissionId());
		
		if (registrations.size() == comission.get().getStudentLimit()) {
			throw new OverLimitException(
					"Еhe limit of the allowed number of students"
					+ " in this commission has been reached");
		}
		for (StudentComission registration : registrations) {
			if (registration.getStudentId() == student.get().getId()) {
				throw new UniquenessViolationException(
						"Such a student is already"
						+ " registered for this commission");
			}
		}
		studentComissionRepo.save(new StudentComission(
				student.get().getId(), 
				dto.getComissionId()));
	}
	
	public void teacherRegistration() {
		
	}
}
