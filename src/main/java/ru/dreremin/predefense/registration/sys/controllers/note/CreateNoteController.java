package ru.dreremin.predefense.registration.sys.controllers.note;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.dreremin.predefense.registration.sys.dto.request.NoteDto;
import ru.dreremin.predefense.registration.sys.dto.response.StatusDto;
import ru.dreremin.predefense.registration.sys.services.note.CreateNoteService;

@Slf4j
@RequiredArgsConstructor
@RestController
public class CreateNoteController {
	
	private final CreateNoteService service;

	@PutMapping(value = "/create-note", consumes = "application/json")
	public StatusDto createNote(@Valid @RequestBody NoteDto dto) 
			throws MethodArgumentNotValidException, 
			HttpMessageNotReadableException,
			EntityNotFoundException {
		service.createNote(dto);
		log.info("CreateNoteController."
				+ "createNote() is success");
		return new StatusDto(200, "Ok");
	}
}
