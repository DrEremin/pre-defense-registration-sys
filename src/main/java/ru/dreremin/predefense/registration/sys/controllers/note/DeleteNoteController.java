package ru.dreremin.predefense.registration.sys.controllers.note;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import ru.dreremin.predefense.registration.sys.dto.response.StatusResponseDto;
import ru.dreremin.predefense.registration.sys.services.note.DeleteNoteService;

@Slf4j
@RequiredArgsConstructor
@RestController
public class DeleteNoteController {
	
	private final DeleteNoteService deleteNoteService;
	
	@DeleteMapping("/note/commission/{id}")
	public ResponseEntity<StatusResponseDto> createNote(
			@PathVariable(value = "id")
			@Min(1)
			@Max(Integer.MAX_VALUE)
			int commissionId) {
		
		deleteNoteService.deleteNote(commissionId);
		log.info("DeleteNoteController.deleteNote() success");
		return ResponseEntity.ok(new StatusResponseDto(200, "Ok"));
	}
}
