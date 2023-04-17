package ru.dreremin.predefense.registration.sys.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.dreremin.predefense.registration.sys.models.TeacherCommission;

@Repository
public interface TeacherCommissionRepository 
		extends JpaRepository<TeacherCommission, Long> {
	
	List<TeacherCommission> findAllByCommissionId(int commissionId);
	
	List<TeacherCommission> findAllByTeacherId(long teacherId);
	
	Optional<TeacherCommission> findByTeacherIdAndCommissionId(
			int teacherId, 
			int commissionId);
	
	void deleteAllByTeacherId(int teacherId);
	
	void deleteAllByCommissionId(int commissionId);
}
