package ru.dreremin.predefense.registration.sys.controllers.note;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.dreremin.predefense.registration.sys.dto.response.NoteResponseDto;
import ru.dreremin.predefense.registration.sys.services.note.ReadNoteService;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ReadNoteController {
	
	private final ReadNoteService readNoteService;
	
	@GetMapping(value = "/admin/read/note/by-commission-id/{id}")
	public ResponseEntity<NoteResponseDto> readNote(
			@PathVariable(value = "id") int id) {
		
		NoteResponseDto dto = readNoteService.readNote(id);
		
		log.info("ReadNoteController.readNote() success");
		return ResponseEntity.ok(dto);
	}
}
