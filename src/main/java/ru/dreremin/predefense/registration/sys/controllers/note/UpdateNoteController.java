package ru.dreremin.predefense.registration.sys.controllers.note;

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

import ru.dreremin.predefense.registration.sys.dto.request.NoteRequestDto;
import ru.dreremin.predefense.registration.sys.dto.response.StatusResponseDto;
import ru.dreremin.predefense.registration.sys.services.note.UpdateNoteService;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UpdateNoteController {

	private final UpdateNoteService updateNoteService;
	
	@PatchMapping("commission/{id}/note")
	public ResponseEntity<StatusResponseDto> updateNote(
			@PathVariable(value = "id")
			@Min(1)
			@Max(Integer.MAX_VALUE)
			int commissionId,
			@Valid @RequestBody	NoteRequestDto dto) {
		
		updateNoteService.updateNote(commissionId, dto);
		log.info("UpdateNoteController.updateNote() success");
		return ResponseEntity.ok(new StatusResponseDto(200, "Ok"));
	}
}
