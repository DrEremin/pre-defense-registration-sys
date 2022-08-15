package ru.dreremin.predefense.registration.sys.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.dreremin.predefense.registration.sys.models.TeacherComission;

@Repository
public interface TeacherComissionRepository 
		extends JpaRepository<TeacherComission, Long> {
	
	List<TeacherComission> findByComissionId(int comissionId);
}
