package ru.dreremin.predefense.registration.sys.repositories;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ru.dreremin.predefense.registration.sys.models.TeacherEntry;

@Repository
public interface TeacherEntryRepository 
		extends JpaRepository<TeacherEntry, Long> {
	
	@Query("select new TeacherEntry(" +
			"tc.id, " +
			"p.lastName, " +
			"p.firstName, " +
			"p.patronymic) " +
		"from TeacherComission tc " +
			"join Teacher t on tc.teacherId = t.id " +
			"join Person p on t.personId = p.id " +
		"where tc.comissionId = :comId")
	List<TeacherEntry> findByComissionId(
		@Param("comId") int comissionId, Sort sort);
}
