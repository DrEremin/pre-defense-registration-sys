package ru.dreremin.predefense.registration.sys.services.admin;

import javax.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import ru.dreremin.predefense.registration.sys.dto.request
		 .AdministratorRequestDto;
import ru.dreremin.predefense.registration.sys.exceptions
		 .NotReadableRequestParameterException;
import ru.dreremin.predefense.registration.sys.exceptions
		 .UniquenessViolationException;
import ru.dreremin.predefense.registration.sys.models.Actor;
import ru.dreremin.predefense.registration.sys.repositories.ActorRepository;

@RequiredArgsConstructor
@Service
public class UpdateAdministratorService {
	
	private final ActorRepository actorRepository;
	
	private final PasswordEncoder passwordEncoder; 
	
	@Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = { 
			EntityNotFoundException.class,  
			UniquenessViolationException.class,
			NotReadableRequestParameterException.class })
	public void updateAdministrator(
			AdministratorRequestDto dto, 
			long actorId) {
		
		Actor actor = actorRepository.findById(actorId)
				.orElseThrow(() -> new EntityNotFoundException(
						"User with this ID does not exsits"));
		if (dto.getLogin() != null) {
			if (actorRepository.findByLogin(dto.getLogin()).isPresent()) {
				throw new UniquenessViolationException(
						"User with this login already exists");
			}
			checkLengthOfLoginOrPassword(dto.getLogin(), "\"login\"");
			actor.setLogin(dto.getLogin());
		}
		if (dto.getPassword() != null) {
			checkLengthOfLoginOrPassword(dto.getLogin(), "\"password\"");
			actor.setPassword(passwordEncoder.encode(dto.getPassword()));
		}
	}
	
	private void checkLengthOfLoginOrPassword(
			String verifiable, 
			String message) {
		if (verifiable.length() < 2 || verifiable.length() > 20) {
			throw new NotReadableRequestParameterException(
					"Invalid format request body field " + message);
		}
	}
}
