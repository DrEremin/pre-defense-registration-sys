package ru.dreremin.predefense.registration.sys.services.person;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.dreremin.predefense.registration.sys.dto.request.PersonDto;
import ru.dreremin.predefense.registration.sys.exceptions
		 .UniquenessViolationException;
import ru.dreremin.predefense.registration.sys.factories.EntitiesFactory;
import ru.dreremin.predefense.registration.sys.models.Actor;
import ru.dreremin.predefense.registration.sys.models.Email;
import ru.dreremin.predefense.registration.sys.models.Person;
import ru.dreremin.predefense.registration.sys.repositories
		 .ActorRepository;
import ru.dreremin.predefense.registration.sys.repositories.EmailRepository;
import ru.dreremin.predefense.registration.sys.repositories.PersonRepository;

@Slf4j
@RequiredArgsConstructor
@Service
public class CreatePersonService {
	
	private final PersonRepository personRepo;
	private final ActorRepository actorRepo;
	private final EmailRepository emailRepo;
	private final PasswordEncoder passwordEncoder;
	
	public Person createPerson(PersonDto dto, String role) 
			throws UniquenessViolationException {
		
		try{
			if (actorRepo.existsByLogin(dto.getLogin())) {
				throw new UniquenessViolationException(
						"The user with this login already exists");
			}
			if (emailRepo.existsByBox(dto.getEmail())) {
				throw new UniquenessViolationException(
						"The user with this email already exists");
			}
		} catch (UniquenessViolationException e) {
			log.warn(e.getMessage());
			throw e;
		}
		String encodedPassword = passwordEncoder.encode(dto.getPassword());
		log.debug("raw password" + dto.getPassword());
		log.debug("encodedPassword" + encodedPassword);
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

