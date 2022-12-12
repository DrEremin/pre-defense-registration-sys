package ru.dreremin.predefense.registration.sys.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.dreremin.predefense.registration.sys.models.TeacherCommission;

@Repository
public interface TeacherCommissionRepository 
		extends JpaRepository<TeacherCommission, Long> {
	
	List<TeacherCommission> findAllByComissionId(int comissionId);
	
	List<TeacherCommission> findAllByTeacherId(long teacherId);
	
	Optional<TeacherCommission> findByTeacherIdAndComissionId(int teacherId, 
															 int comissionId);
	
	void deleteAllByTeacherId(int teacherId);
}
