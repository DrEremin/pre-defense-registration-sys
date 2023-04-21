package ru.dreremin.predefense.registration.sys.services.admin;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import ru.dreremin.predefense.registration.sys.dto.request.AdminRequestDto;
import ru.dreremin.predefense.registration.sys.exceptions
		 .UniquenessViolationException;
import ru.dreremin.predefense.registration.sys.models.Actor;
import ru.dreremin.predefense.registration.sys.models.Administrator;
import ru.dreremin.predefense.registration.sys.repositories.ActorRepository;
import ru.dreremin.predefense.registration.sys.repositories
		 .AdministratorRepository;
import ru.dreremin.predefense.registration.sys.util.Role;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateAdministratorService {
	
	private final AdministratorRepository administratorRepo;
	
	private final ActorRepository actorRepo;
	
	private final PasswordEncoder passwordEncoder;
	
	@Transactional(isolation = Isolation.SERIALIZABLE,
            rollbackFor = { UniquenessViolationException.class })
	public void createAdmin(AdminRequestDto dto) {
		
		if (actorRepo.existsByLogin(dto.getLogin())) {
			throw new UniquenessViolationException(
						"The user with this login already exists");
		}
		String encodedPassword = passwordEncoder.encode(dto.getPassword());
		Actor actor = actorRepo.save(new Actor(
				dto.getLogin(), 
				encodedPassword, 
				Role.ADMIN.getRole()));
		administratorRepo.save(new Administrator(actor.getId()));
		log.info("The student created successfully");
	}
}
