package ru.dreremin.predefense.registration.sys.services.admin;

import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import ru.dreremin.predefense.registration.sys.models.Actor;
import ru.dreremin.predefense.registration.sys.models.Administrator;
import ru.dreremin.predefense.registration.sys.repositories.ActorRepository;
import ru.dreremin.predefense.registration.sys.repositories
		 .AdministratorRepository;

@RequiredArgsConstructor
@Service
public class DeleteAdministratorService {

	private final AdministratorRepository administratorRepository;
	private final ActorRepository actorRepository;
	
	@Transactional(isolation = Isolation.SERIALIZABLE, 
			rollbackFor = { UsernameNotFoundException.class, 
					EntityNotFoundException.class })
	public void deleteAdmin(long actorId) {
		
		Optional<Actor> actorOpt = actorRepository.findById(actorId);
		
		if (actorOpt.isEmpty()) {
			throw new UsernameNotFoundException(
					"User with this id does not exist");
		}
		
		Optional<Administrator> administratorOpt = administratorRepository
				.findByActorId(actorId);
		
		if (administratorOpt.isEmpty()) {
			throw new EntityNotFoundException(
					"User with this id is not an administrator");
		}
		administratorRepository.delete(administratorOpt.get());
		actorRepository.delete(actorOpt.get());
	}
}
