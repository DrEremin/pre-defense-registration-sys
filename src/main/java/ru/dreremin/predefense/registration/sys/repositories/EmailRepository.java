package ru.dreremin.predefense.registration.sys.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ru.dreremin.predefense.registration.sys.models.Email;

@Repository
public interface EmailRepository extends JpaRepository<Email, String>{
	
	Optional<Email> findByBox(String box);
	
	default boolean existsByBox(String box) {
		return findByBox(box).isPresent();
	}
	
	@Query("select e from Email e join Student s on e.personId = s.personId")
	List<Email> findAllByStudent();
	
	@Query("select e from Email e join Teacher t on e.personId = t.personId")
	List<Email> findAllByTeacher();
	
	Optional<Email> findByPersonId(long personId);
}

