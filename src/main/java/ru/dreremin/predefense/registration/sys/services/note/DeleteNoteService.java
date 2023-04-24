package ru.dreremin.predefense.registration.sys.services.note;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import ru.dreremin.predefense.registration.sys.models.Note;
import ru.dreremin.predefense.registration.sys.repositories
		 .CommissionRepository;
import ru.dreremin.predefense.registration.sys.repositories.NoteRepository;

@RequiredArgsConstructor
@Service
public class DeleteNoteService {

	private final NoteRepository noteRepository;
	
	private final CommissionRepository commissionRepository;
	
	@Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = { 
			EntityNotFoundException.class})
	public void deleteNote(int commissionId) {
		
		commissionRepository.findById(commissionId).orElseThrow(
				() -> new EntityNotFoundException(
						"Commission with this ID does not exists"));
		
		Note note = noteRepository.findByCommissionId(commissionId)
				.orElseThrow(() -> new EntityNotFoundException(
						"Note with this commission ID does not exists"));
		
		noteRepository.delete(note);
	}
}
