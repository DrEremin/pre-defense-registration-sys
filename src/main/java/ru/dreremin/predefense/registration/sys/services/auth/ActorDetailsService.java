package ru.dreremin.predefense.registration.sys.services.auth;

import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.dreremin.predefense.registration.sys.dto.request.AuthenticationDto;
import ru.dreremin.predefense.registration.sys.models.Actor;
import ru.dreremin.predefense.registration.sys.repositories.ActorRepository;
import ru.dreremin.predefense.registration.sys.security.ActorDetails;
import ru.dreremin.predefense.registration.sys.security.JwtTokenProvider;

@RequiredArgsConstructor
@Service
public class ActorDetailsService implements UserDetailsService {

	private final ActorRepository actorRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) {
		
		Optional<Actor> actorOpt = actorRepo.findByLogin(username);
		
		if (actorOpt.isEmpty()) {
			throw new UsernameNotFoundException(
					"There is not exists person with this login");
		}
		return new ActorDetails(actorOpt.get());
	}
}
