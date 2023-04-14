package ru.dreremin.predefense.registration.sys.services.note;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import ru.dreremin.predefense.registration.sys.dto.request.NoteRequestDto;
import ru.dreremin.predefense.registration.sys.models.Commission;
import ru.dreremin.predefense.registration.sys.models.Note;
import ru.dreremin.predefense.registration.sys.repositories
		 .CommissionRepository;
import ru.dreremin.predefense.registration.sys.repositories.NoteRepository;

@RequiredArgsConstructor
@Service
public class CreateNoteService {
	
	private final NoteRepository noteRepo;
	private final CommissionRepository commissionRepo;
	
	@Transactional(isolation = Isolation.SERIALIZABLE,
			rollbackFor = {EntityNotFoundException.class})
	public void createNote(NoteRequestDto dto) throws EntityNotFoundException {
		
		Optional<Commission> commissionOpt = 
				commissionRepo.findById(dto.getCommissionId());
		if (commissionOpt.isEmpty()) {
			throw new EntityNotFoundException(
					"There is not exists comission with this Id");
		}
		noteRepo.save(new Note(dto.getCommissionId(), dto.getNoteContent()));
	}
}
