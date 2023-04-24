package ru.dreremin.predefense.registration.sys.services.admin;

import javax.persistence.EntityNotFoundException;
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
		String newLogin = dto.getLogin();
		
		if (newLogin != null) {
			if (actorRepository.findByLogin(newLogin).isPresent()) {
				throw new UniquenessViolationException(
						"User with this login already exists");
			}
			if (newLogin.length() < 2 || newLogin.length() > 20) {
				throw new NotReadableRequestParameterException(
						"Invalid format request body field \"login\"");
			}
			actor.setLogin(newLogin);
		}
	}
}
