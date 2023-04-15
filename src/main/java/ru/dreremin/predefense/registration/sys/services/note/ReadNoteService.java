package ru.dreremin.predefense.registration.sys.services.note;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.dreremin.predefense.registration.sys.models.Note;
import ru.dreremin.predefense.registration.sys.repositories
		 .CommissionRepository;
import ru.dreremin.predefense.registration.sys.repositories.NoteRepository;
import ru.dreremin.predefense.registration.sys.dto.response.NoteResponseDto;

@RequiredArgsConstructor
@Service
public class ReadNoteService {

	private final NoteRepository noteRepository;
	
	private final CommissionRepository commissionRepository;
	
	public NoteResponseDto readNote(int commissionId) {
		
		commissionRepository.findById(commissionId).orElseThrow(
				() -> new EntityNotFoundException("There is not exists "
						+ "commission with this Id"));
		
		Optional<Note> noteOpt = noteRepository.findByCommissionId(
				commissionId);
		
		return new NoteResponseDto(noteOpt.isPresent() 
				? noteOpt.get().getNoteContent() 
				: "") ;
	}
}
