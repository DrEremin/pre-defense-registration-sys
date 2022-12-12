package ru.dreremin.predefense.registration.sys.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.dreremin.predefense.registration.sys.models.StudentCommission;

@Repository
public interface StudentCommissionRepository 
		extends JpaRepository<StudentCommission, Long> {

	List<StudentCommission> findAllByComissionId(int comissionId);
	
	Optional<StudentCommission> findByStudentId(long studentId);
	
	void deleteAllByStudentId(long studentId);
}
