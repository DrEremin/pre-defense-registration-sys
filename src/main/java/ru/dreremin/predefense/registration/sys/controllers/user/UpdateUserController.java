package ru.dreremin.predefense.registration.sys.controllers.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.dreremin.predefense.registration.sys.dto.response.StatusResponseDto;
import ru.dreremin.predefense.registration.sys.services.user.UpdateUserService;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UpdateUserController {

	private final UpdateUserService updateUserService;
	
	@PutMapping("/user/update/password")
	public ResponseEntity<StatusResponseDto> updatePassword(
			@RequestParam() String value) {
		updateUserService.updatePassword(value);
		log.info("UpdateUserController.updatePassword() success");
		return ResponseEntity.ok(new StatusResponseDto(200, "Ok"));
	}
}
