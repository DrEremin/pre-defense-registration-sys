package ru.dreremin.predefense.registration.sys.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ru.dreremin.predefense.registration.sys.models.Note;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
	
	Optional<Note> findByCommissionId(int commissionId);
	
	void deleteByCommissionId(int commissionId);
	
	@Query("update Note n set n.noteContent = :content where n.id = :id")
	void updateNoteContent(
			@Param(value = "content") String content, 
			@Param(value = "id") long id);
}
