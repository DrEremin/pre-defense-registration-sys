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

	private final AdministratorRepository adminRepo;
	private final ActorRepository actorRepo;
	
	@Transactional(isolation = Isolation.SERIALIZABLE, 
			rollbackFor = { UsernameNotFoundException.class, 
					EntityNotFoundException.class })
	public void deleteAdmin(String login) {
		
		Optional<Actor> actorOpt = actorRepo.findByLogin(login);
		
		if (actorOpt.isEmpty()) {
			throw new UsernameNotFoundException(
					"User with this login does not exist");
		}
		
		Optional<Administrator> adminOpt = adminRepo.findByActorId(
				actorOpt.get().getId());
		
		if (adminOpt.isEmpty()) {
			throw new EntityNotFoundException(
					"Administrator with this login does not exist");
		}
		adminRepo.delete(adminOpt.get());
		actorRepo.delete(actorOpt.get());
	}
}
