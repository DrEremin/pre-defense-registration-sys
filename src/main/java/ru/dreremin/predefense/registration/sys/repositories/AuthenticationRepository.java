package ru.dreremin.predefense.registration.sys.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.dreremin.predefense.registration.sys.models.Authentication;

@Repository
public interface AuthenticationRepository 
		extends JpaRepository<Authentication, String>{
	
	Optional<Authentication> findByLogin(String login);
	
	default boolean existsByLogin(String login) {
		return findByLogin(login).isPresent();
	}
}

 