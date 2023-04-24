package ru.dreremin.predefense.registration.sys.services.note;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import ru.dreremin.predefense.registration.sys.dto.request.NoteRequestDto;
import ru.dreremin.predefense.registration.sys.exceptions
		 .UniquenessViolationException;
import ru.dreremin.predefense.registration.sys.models.Note;
import ru.dreremin.predefense.registration.sys.repositories.NoteRepository;

@RequiredArgsConstructor
@Service
public class CreateNoteService {
	
	private final NoteRepository noteRepository;
	
	@Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = { 
			UniquenessViolationException.class })
	public void createNote(int commissionId, NoteRequestDto dto) {
		
		Optional<Note> noteOpt = noteRepository
				.findByCommissionId(commissionId);
		
		if (noteOpt.isPresent()) {
			throw new UniquenessViolationException(
					"Note with this commission ID already exists");
		}
		noteRepository.save(new Note(commissionId, dto.getContent()));
	}
}
