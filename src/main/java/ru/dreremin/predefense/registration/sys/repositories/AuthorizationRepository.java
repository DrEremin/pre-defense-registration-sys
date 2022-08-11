package ru.dreremin.predefense.registration.sys.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.dreremin.predefense.registration.sys.models.Authorization;

@Repository
public interface AuthorizationRepository 
		extends JpaRepository<Authorization, String>{
	
	Optional<Authorization> findByLogin(String login);
	
	default boolean existsByLogin(String login) {
		return findByLogin(login).isPresent();
	}
}

