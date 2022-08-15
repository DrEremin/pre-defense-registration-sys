package ru.dreremin.predefense.registration.sys.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.dreremin.predefense.registration.sys.models.Teacher;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Integer> {
	
	Optional<Teacher> findByPersonId(Long id);
	
	default boolean existsByPersonId(Long id) {
		return findByPersonId(id).isPresent();
	}
}
