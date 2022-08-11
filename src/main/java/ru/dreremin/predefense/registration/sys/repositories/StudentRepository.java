package ru.dreremin.predefense.registration.sys.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.dreremin.predefense.registration.sys.models.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long>{
	
}
