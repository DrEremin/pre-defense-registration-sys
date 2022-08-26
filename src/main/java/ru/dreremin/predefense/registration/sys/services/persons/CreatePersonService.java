package ru.dreremin.predefense.registration.sys.services.persons;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import ru.dreremin.predefense.registration.sys.dto.requestdto.PersonDto;
import ru.dreremin.predefense.registration.sys.exceptions
		 .UniquenessViolationException;
import ru.dreremin.predefense.registration.sys.factories.EntitiesFactory;
import ru.dreremin.predefense.registration.sys.models.Authentication;
import ru.dreremin.predefense.registration.sys.models.Email;
import ru.dreremin.predefense.registration.sys.models.Person;
import ru.dreremin.predefense.registration.sys.repositories
		 .AuthenticationRepository;
import ru.dreremin.predefense.registration.sys.repositories.EmailRepository;
import ru.dreremin.predefense.registration.sys.repositories.PersonRepository;

@Slf4j
@RequiredArgsConstructor
@Service
public class CreatePersonService {
	
	private final PersonRepository personRepo;
	private final AuthenticationRepository authorRepo;
	private final EmailRepository emailRepo;
	
	public Person createPerson(PersonDto dto) 
			throws UniquenessViolationException {
		
		try{
			if (authorRepo.existsByLogin(dto.getLogin())) {
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
		
		Person person = personRepo.save(EntitiesFactory.createPerson(dto));
		
		authorRepo.save(new Authentication(dto.getLogin(), 
										  dto.getPassword(), 
										  person.getId()));
		emailRepo.save(new Email(dto.getEmail(), person.getId()));
		return person;
	}
}

