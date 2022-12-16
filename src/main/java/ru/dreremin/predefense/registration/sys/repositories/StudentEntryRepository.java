package ru.dreremin.predefense.registration.sys.repositories;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ru.dreremin.predefense.registration.sys.models.StudentEntry;

@Repository
public interface StudentEntryRepository 
		extends JpaRepository<StudentEntry, Long> {

	@Query("select new StudentEntry(" +
				"sc.id, " +
				"p.lastName, " +
				"p.firstName, " +
				"p.patronymic, " +
				"s.groupNumber) " +
			"from StudentCommission sc " +
				"join Student s on sc.studentId = s.id " +
				"join Person p on s.personId = p.id " +
			"where sc.commissionId = :comId")
	List<StudentEntry> findAllByCommissionId(
			@Param("comId") int comissionId, Sort sort);
}
