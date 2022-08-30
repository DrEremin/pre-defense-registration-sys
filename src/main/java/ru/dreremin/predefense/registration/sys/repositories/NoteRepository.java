package ru.dreremin.predefense.registration.sys.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.dreremin.predefense.registration.sys.models.Note;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
	
	Optional<Note> findByComissionId(int comissionId);
}
