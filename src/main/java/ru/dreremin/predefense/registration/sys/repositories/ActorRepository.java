package ru.dreremin.predefense.registration.sys.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.dreremin.predefense.registration.sys.models.Actor;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Long>{
	
	Optional<Actor> findByLogin(String login);
	
	default boolean existsByLogin(String login) {
		return findByLogin(login).isPresent();
	}
}

 