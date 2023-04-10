package ru.dreremin.predefense.registration.sys.services.user;

import org.springframework.stereotype.Service;
import com.auth0.jwt.JWT;
import lombok.RequiredArgsConstructor;

import ru.dreremin.predefense.registration.sys.dto.response.UserDto;
import ru.dreremin.predefense.registration.sys.security.ActorDetails;
import ru.dreremin.predefense.registration.sys.services.auth
		 .ActorDetailsService;

@Service
@RequiredArgsConstructor
public class GetUserService {

	private final ActorDetailsService actorDetailsService;
	
	public ActorDetails getActorDetails(String token) {
		return (ActorDetails) actorDetailsService.loadUserByUsername(JWT
				.decode(token)
				.getClaim("login")
				.asString());
	}
}