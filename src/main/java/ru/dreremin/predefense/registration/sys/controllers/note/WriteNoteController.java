package ru.dreremin.predefense.registration.sys.controllers.note;

import javax.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import ru.dreremin.predefense.registration.sys.dto.request.NoteRequestDto;
import ru.dreremin.predefense.registration.sys.dto.response.StatusResponseDto;
import ru.dreremin.predefense.registration.sys.services.note.WriteNoteService;

@Slf4j
@RequiredArgsConstructor
@RestController
public class WriteNoteController {

	private final WriteNoteService writeNoteService;

	@PutMapping(
			value = "/admin/write/note", 
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<StatusResponseDto> updateNote(
			@Valid @RequestBody NoteRequestDto dto) {
		writeNoteService.addNote(dto);
		log.info("UpdateNoteController.updateNote() is success");
		return ResponseEntity.ok(new StatusResponseDto(200, "Ok"));
	}
}
