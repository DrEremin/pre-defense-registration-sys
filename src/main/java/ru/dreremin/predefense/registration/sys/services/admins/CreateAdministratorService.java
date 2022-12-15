package ru.dreremin.predefense.registration.sys.services.admins;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.dreremin.predefense.registration.sys.dto.requestdto.AdminDto;
import ru.dreremin.predefense.registration.sys.dto.requestdto.StudentDto;
import ru.dreremin.predefense.registration.sys.exceptions.UniquenessViolationException;
import ru.dreremin.predefense.registration.sys.factories.EntitiesFactory;
import ru.dreremin.predefense.registration.sys.models.Actor;
import ru.dreremin.predefense.registration.sys.models.Administrator;
import ru.dreremin.predefense.registration.sys.repositories.ActorRepository;
import ru.dreremin.predefense.registration.sys.repositories.AdministratorRepository;
import ru.dreremin.predefense.registration.sys.repositories.StudentRepository;
import ru.dreremin.predefense.registration.sys.security.JwtTokenProvider;
import ru.dreremin.predefense.registration.sys.services.persons.CreatePersonService;
import ru.dreremin.predefense.registration.sys.services.students.CreateStudentService;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateAdministratorService {
	
	private final AdministratorRepository administratorRepo;
	private final ActorRepository actorRepo;
	private final JwtTokenProvider jwtTokenProvider;
	private final PasswordEncoder passwordEncoder;
	
	@Transactional(isolation = Isolation.SERIALIZABLE,
            rollbackFor = { UniquenessViolationException.class })
	public String createAdmin(AdminDto dto) {
		
		if (actorRepo.existsByLogin(dto.getLogin())) {
			throw new UniquenessViolationException(
						"The user with this login already exists");
		}
		String encodedPassword = passwordEncoder.encode(dto.getPassword());
		Actor actor = actorRepo.save(new Actor(
				dto.getLogin(), 
				encodedPassword, 
				"ROLE_ADMIN"));
		administratorRepo.save(new Administrator(actor.getId()));
		log.info("The student created successfully");
		return jwtTokenProvider.generateToken(dto.getLogin());
	}
}
