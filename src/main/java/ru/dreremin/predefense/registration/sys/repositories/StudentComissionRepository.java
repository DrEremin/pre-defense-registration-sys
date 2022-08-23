package ru.dreremin.predefense.registration.sys.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.dreremin.predefense.registration.sys.models.StudentComission;

@Repository
public interface StudentComissionRepository 
		extends JpaRepository<StudentComission, Long> {

	List<StudentComission> findByComissionId(int comissionId);
	
	Optional<StudentComission> findByStudentId(long studentId);
	
}
