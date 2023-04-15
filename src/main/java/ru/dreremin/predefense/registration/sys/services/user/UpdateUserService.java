package ru.dreremin.predefense.registration.sys.services.user;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import ru.dreremin.predefense.registration.sys.models.Actor;
import ru.dreremin.predefense.registration.sys.repositories.ActorRepository;
import ru.dreremin.predefense.registration.sys.security.ActorDetails;

@RequiredArgsConstructor
@Service
public class UpdateUserService {
	
	private final ActorRepository actorRepository;
	
	private final PasswordEncoder passwordEncoder; 
	
	public void updatePassword(String password) {
		
		Actor actor = ((ActorDetails) SecurityContextHolder.getContext()
				.getAuthentication()
				.getPrincipal())
				.getActor();
		actor.setPassword(passwordEncoder.encode(password));
		actorRepository.save(actor);
	}
}
