package ru.dreremin.predefense.registration.sys.services.person;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import ru.dreremin.predefense.registration.sys.dto.request.PersonRequestDto;
import ru.dreremin.predefense.registration.sys.exceptions
		 .UniquenessViolationException;
import ru.dreremin.predefense.registration.sys.models.Actor;
import ru.dreremin.predefense.registration.sys.models.Email;
import ru.dreremin.predefense.registration.sys.models.Person;
import ru.dreremin.predefense.registration.sys.repositories
		 .ActorRepository;
import ru.dreremin.predefense.registration.sys.repositories.EmailRepository;
import ru.dreremin.predefense.registration.sys.repositories.PersonRepository;
import ru.dreremin.predefense.registration.sys.util.EntitiesFactory;


@RequiredArgsConstructor
@Service
public class CreatePersonService {
	
	private final PersonRepository personRepo;
	
	private final ActorRepository actorRepo;
	
	private final EmailRepository emailRepo;
	
	private final PasswordEncoder passwordEncoder;
	
	public Person createPerson(PersonRequestDto dto, String role) {
		
		
		if (actorRepo.existsByLogin(dto.getLogin())) {
			throw new UniquenessViolationException(
						"The user with this login already exists");
		}
		if (emailRepo.existsByBox(dto.getEmail())) {
			throw new UniquenessViolationException(
					"The user with this email already exists");
		}
		
		String encodedPassword = passwordEncoder.encode(dto.getPassword());
		Actor actor = actorRepo.save(new Actor(
				dto.getLogin(), 
				encodedPassword, 
				role));
		Person person = personRepo.save(EntitiesFactory
				.createPerson(dto, actor.getId()));
		emailRepo.save(new Email(dto.getEmail(), person.getId()));
		return person;
	}
}

