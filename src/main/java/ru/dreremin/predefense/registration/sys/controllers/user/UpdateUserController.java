package ru.dreremin.predefense.registration.sys.controllers.user;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import ru.dreremin.predefense.registration.sys.dto.request.PasswordRequestDto;
import ru.dreremin.predefense.registration.sys.dto.response.StatusResponseDto;
import ru.dreremin.predefense.registration.sys.services.user.UpdateUserService;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UpdateUserController {

	private final UpdateUserService updateUserService;
	
	@PatchMapping("/user/{id}")
	public ResponseEntity<StatusResponseDto> updatePassword(
			@PathVariable(value = "id")
			@Min(1)
			@Max(Long.MAX_VALUE)
			long id, 
			@RequestBody 
			@Valid 
			PasswordRequestDto dto) {
		updateUserService.updatePassword(dto, id);
		log.info("UpdateUserController.updatePassword() success");
		return ResponseEntity.ok(new StatusResponseDto(200, "Ok"));
	}
}
