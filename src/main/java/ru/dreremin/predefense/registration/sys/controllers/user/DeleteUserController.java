package ru.dreremin.predefense.registration.sys.controllers.user;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import ru.dreremin.predefense.registration.sys.dto.response.StatusResponseDto;
import ru.dreremin.predefense.registration.sys.services.user.DeleteUserService;

@Slf4j
@RequiredArgsConstructor
@RestController
public class DeleteUserController {

	private final DeleteUserService deleteUserService;
	
	@DeleteMapping("/user/{id}")
	public ResponseEntity<StatusResponseDto> deleteUser(
			@PathVariable (value = "id") 
			@Min(1) 
			@Max(Long.MAX_VALUE) 
			long id) {
		
		deleteUserService.deleteUser(id);
		log.info("DeleteUserController.deleteUser() success");
		return ResponseEntity.ok(new StatusResponseDto(200, "Ok"));
	}
}
