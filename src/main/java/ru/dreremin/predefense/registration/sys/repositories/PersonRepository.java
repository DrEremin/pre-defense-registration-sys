package ru.dreremin.predefense.registration.sys.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.dreremin.predefense.registration.sys.models.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long>{
	
}
