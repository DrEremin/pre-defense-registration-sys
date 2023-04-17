package ru.dreremin.predefense.registration.sys.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.dreremin.predefense.registration.sys.models.Administrator;

@Repository
public interface AdministratorRepository 
		extends JpaRepository<Administrator, Integer> {
	
	Optional<Administrator> findByActorId(long actorId);
}
