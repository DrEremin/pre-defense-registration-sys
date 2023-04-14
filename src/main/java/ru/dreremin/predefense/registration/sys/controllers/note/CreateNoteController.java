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
import ru.dreremin.predefense.registration.sys.dto.request.NoteRequestDto;
import ru.dreremin.predefense.registration.sys.dto.response.StatusResponseDto;
import ru.dreremin.predefense.registration.sys.services.note.CreateNoteService;

@Slf4j
@RequiredArgsConstructor
@RestController
public class CreateNoteController {
	
	private final CreateNoteService service;

	@PutMapping(value = "/create-note", consumes = "application/json")
	public StatusResponseDto createNote(@Valid @RequestBody NoteRequestDto dto) 
			throws MethodArgumentNotValidException, 
			HttpMessageNotReadableException,
			EntityNotFoundException {
		service.createNote(dto);
		log.info("CreateNoteController."
				+ "createNote() is success");
		return new StatusResponseDto(200, "Ok");
	}
}
