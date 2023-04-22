package ru.dreremin.predefense.registration.sys.services.person;

import javax.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import ru.dreremin.predefense.registration.sys.dto.request.PersonRequestDto;
import ru.dreremin.predefense.registration.sys.exceptions
		 .NotReadableRequestParameterException;
import ru.dreremin.predefense.registration.sys.models.Actor;
import ru.dreremin.predefense.registration.sys.models.Email;
import ru.dreremin.predefense.registration.sys.models.Person;
import ru.dreremin.predefense.registration.sys.repositories.ActorRepository;
import ru.dreremin.predefense.registration.sys.repositories.EmailRepository;
import ru.dreremin.predefense.registration.sys.repositories.PersonRepository;

@RequiredArgsConstructor
@Service
public class UpdatePersonService {
	
	private final ActorRepository actorRepository;
	
	private final PersonRepository personRepository;
	
	private final EmailRepository emailRepository;
	
	private final PasswordEncoder passwordEncoder;
	
	@Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = {
			EntityNotFoundException.class, 
			NotReadableRequestParameterException.class})
	public long updatePerson(long actorId, PersonRequestDto dto) {
		
		Actor actor = actorRepository.findById(actorId).orElseThrow(
				() -> new EntityNotFoundException(
						"User with this ID does not exist"));
		
		if (dto.getLogin() != null) {
			actor.setLogin(dto.getLogin());
		}
		if (dto.getPassword() != null) {
			actor.setPassword(passwordEncoder.encode(dto.getPassword()));
		}
		
		Person person = personRepository.findByActorId(actorId)
				.orElseThrow(() -> new EntityNotFoundException(
						"Person with this user ID does not exist"));
		
		if (dto.getLastName() != null) {
			person.setLastName(dto.getLastName());
		}
		if (dto.getFirstName() != null) {
			person.setFirstName(dto.getLastName());
		}
		if (dto.getPatronymic() != null) {
			person.setPatronymic(dto.getPatronymic());
		}
		
		Email email = emailRepository.findByPersonId(person.getId())
				.orElseThrow(() -> new EntityNotFoundException(
						"Email for this person does not exist"));
		
		if (dto.getEmail() != null 
				&& !dto.getEmail().equals(email.getAddress())) {
			emailValidation(dto.getEmail());
			email.setAddress(dto.getEmail());
		}
		return person.getId();
	}
	
	private void emailValidation(String address) {
		if (!address.matches(".+@.+\\..{1,3}") 
				|| address.length() < 8 
				|| address.length() > 40) {
			throw new NotReadableRequestParameterException(
					"Invalid format request body field \"email\"");
		}
	}
}
