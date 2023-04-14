package ru.dreremin.predefense.registration.sys.controllers.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.dreremin.predefense.registration.sys.dto.response.UserResponseDto;
import ru.dreremin.predefense.registration.sys.security.ActorDetails;
import ru.dreremin.predefense.registration.sys.services.user.GetUserService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class GetUserController {
	
	private final GetUserService getUserService;
	
	@GetMapping(value = "/get")
	public ResponseEntity<UserResponseDto> getUser(
			@RequestHeader(value = "Authorization") String jwt) {
		
		ActorDetails actorDetails = getUserService.getActorDetails(
				jwt.substring(7));
		
		log.info("GetUserController.getUser() success");
		return ResponseEntity.ok(new UserResponseDto(
				actorDetails.getId(), 
				actorDetails.getUsername(), 
				actorDetails.getRole()));
	}

}
