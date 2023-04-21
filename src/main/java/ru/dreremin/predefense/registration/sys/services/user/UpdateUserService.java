package ru.dreremin.predefense.registration.sys.services.user;

import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import ru.dreremin.predefense.registration.sys.dto.request
		 .AuthenticationRequestDto;
import ru.dreremin.predefense.registration.sys.models.Actor;
import ru.dreremin.predefense.registration.sys.repositories.ActorRepository;

@RequiredArgsConstructor
@Service
public class UpdateUserService {
	
	private final ActorRepository actorRepository;
	
	private final PasswordEncoder passwordEncoder; 
	
	@Transactional(isolation = Isolation.SERIALIZABLE)
	public void updatePassword(AuthenticationRequestDto dto) {
		
		Optional<Actor> actorOpt = actorRepository.findByLogin(dto.getLogin());
		
		if (actorOpt.isEmpty()) {
			throw new EntityNotFoundException(
					"User with this login does not exist");
		}
		actorOpt.get().setPassword(passwordEncoder.encode(dto.getPassword()));
	}
}
