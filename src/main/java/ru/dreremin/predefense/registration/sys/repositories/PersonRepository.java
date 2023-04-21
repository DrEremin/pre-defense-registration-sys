package ru.dreremin.predefense.registration.sys.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ru.dreremin.predefense.registration.sys.models.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long>{
	
	Optional<Person> findByActorId(long actorId);
}
