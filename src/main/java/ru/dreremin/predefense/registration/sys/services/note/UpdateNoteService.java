package ru.dreremin.predefense.registration.sys.services.note;

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
public class UpdateNoteService {
	
	private final NoteRepository noteRepository;
	
	private final CommissionRepository commissionRepository;
	
	@Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = { 
			EntityNotFoundException.class })
	public void updateNote(int commissionId, NoteRequestDto dto) {
		
		commissionRepository.findById(commissionId).orElseThrow(
				() -> new EntityNotFoundException(
						"Commission with this ID does not exists"));
		
		Note note = noteRepository.findByCommissionId(commissionId)
				.orElseThrow(() -> new EntityNotFoundException(
						"Note with this commission ID does not exists"));
		
		note.setNoteContent(dto.getContent());
	}
}
