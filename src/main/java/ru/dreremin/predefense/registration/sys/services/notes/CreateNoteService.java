package ru.dreremin.predefense.registration.sys.services.notes;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import ru.dreremin.predefense.registration.sys.dto.requestdto.impl.NoteDto;
import ru.dreremin.predefense.registration.sys.models.Comission;
import ru.dreremin.predefense.registration.sys.models.Note;
import ru.dreremin.predefense.registration.sys.repositories
		 .ComissionRepository;
import ru.dreremin.predefense.registration.sys.repositories.NoteRepository;

@RequiredArgsConstructor
@Service
public class CreateNoteService {
	
	private final NoteRepository noteRepo;
	private final ComissionRepository comissionRepo;
	
	@Transactional(isolation = Isolation.SERIALIZABLE,
			rollbackFor = {EntityNotFoundException.class})
	public void createNote(NoteDto dto) throws EntityNotFoundException {
		
		Optional<Comission> comissionOpt = 
				comissionRepo.findById(dto.getComissionId());
		if (comissionOpt.isEmpty()) {
			throw new EntityNotFoundException(
					"There is not exists comission with this Id");
		}
		noteRepo.save(new Note(dto.getComissionId(), dto.getNoteContent()));
	}
}
