package ru.dreremin.predefense.registration.sys.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.dreremin.predefense.registration.sys.models.TeacherComission;

@Repository
public interface TeacherComissionRepository 
		extends JpaRepository<TeacherComission, Long> {
	
	List<TeacherComission> findByComissionId(int comissionId);
	
	List<TeacherComission> findByTeacherId(long teacherId);
	
	Optional<TeacherComission> findByTeacherIdAndComissionId(long teacherId, 
															 int comissionId);
}
