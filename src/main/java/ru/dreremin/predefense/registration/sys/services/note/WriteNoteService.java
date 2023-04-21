package ru.dreremin.predefense.registration.sys.services.note;

import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import ru.dreremin.predefense.registration.sys.dto.request.NoteRequestDto;
import ru.dreremin.predefense.registration.sys.models.Note;
import ru.dreremin.predefense.registration.sys.repositories
		 .CommissionRepository;
import ru.dreremin.predefense.registration.sys.repositories.NoteRepository;

@RequiredArgsConstructor
@Service
public class WriteNoteService {

	private final NoteRepository noteRepository;
	
	private final CommissionRepository commissionRepository;
	
	@Transactional(
			isolation = Isolation.SERIALIZABLE,
			rollbackFor = {EntityNotFoundException.class})
	public void writeNote(NoteRequestDto dto) {
		
		commissionRepository.findById(dto.getCommissionId()).orElseThrow(
						() -> new EntityNotFoundException("There is not "
								+ "exists commission with this Id"));
		
		Optional<Note> noteOpt = noteRepository.findByCommissionId(
				dto.getCommissionId());
		
		if (noteOpt.isEmpty()) {
			if (dto.getContent().length() > 0) { 
				noteRepository.save(new Note(
						dto.getCommissionId(), 
						dto.getContent()));
			}
		} else {
			if (dto.getContent().length() > 0) {
				noteOpt.get().setNoteContent(dto.getContent());
			} else {
				noteRepository.delete(noteOpt.get());
			}
		}
	}
}
