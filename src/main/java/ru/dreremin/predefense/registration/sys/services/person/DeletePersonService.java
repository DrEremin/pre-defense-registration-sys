package ru.dreremin.predefense.registration.sys.services.person;

import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.dreremin.predefense.registration.sys.models.Email;
import ru.dreremin.predefense.registration.sys.models.Person;
import ru.dreremin.predefense.registration.sys.repositories.EmailRepository;
import ru.dreremin.predefense.registration.sys.repositories.PersonRepository;

@RequiredArgsConstructor
@Service
public class DeletePersonService {
	
	private final EmailRepository emailRepo;
	private final PersonRepository personRepo;

	public void deletePersonAndEmail(long actorId) {
		
		Optional<Person> personOpt = personRepo.findByActorId(actorId);
		
		if (personOpt.isPresent()) {
			
			Optional<Email> emailOpt = emailRepo.findByPersonId(
					personOpt.get().getId());
			
			if (emailOpt.isPresent()) { emailRepo.delete(emailOpt.get()); }
			personRepo.delete(personOpt.get());
		}
	}
}
