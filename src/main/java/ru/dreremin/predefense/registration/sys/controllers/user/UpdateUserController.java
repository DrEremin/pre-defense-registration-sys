package ru.dreremin.predefense.registration.sys.controllers.user;

import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import ru.dreremin.predefense.registration.sys.dto.request
		 .AuthenticationRequestDto;
import ru.dreremin.predefense.registration.sys.dto.response.StatusResponseDto;
import ru.dreremin.predefense.registration.sys.services.user.UpdateUserService;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UpdateUserController {

	private final UpdateUserService updateUserService;
	
	@PatchMapping("/user/update/password")
	public ResponseEntity<StatusResponseDto> updatePassword(
			@RequestBody @Valid AuthenticationRequestDto dto) {
		updateUserService.updatePassword(dto);
		log.info("UpdateUserController.updatePassword() success");
		return ResponseEntity.ok(new StatusResponseDto(200, "Ok"));
	}
}
