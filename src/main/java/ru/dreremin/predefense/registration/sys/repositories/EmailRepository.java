package ru.dreremin.predefense.registration.sys.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.dreremin.predefense.registration.sys.models.Email;

@Repository
public interface EmailRepository extends JpaRepository<Email, String>{
	
	Optional<Email> findByBox(String box);
	
	default boolean existsByBox(String box) {
		return findByBox(box).isPresent();
	}
}

