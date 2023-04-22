package ru.dreremin.predefense.registration.sys.repositories;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ru.dreremin.predefense.registration.sys.models.StudentCommission;

@Repository
public interface StudentCommissionRepository 
		extends JpaRepository<StudentCommission, Long> {

	List<StudentCommission> findAllByCommissionId(int commissionId);
	
	@Query("select new StudentCommission("
			+ "sc.id, "
			+ "sc.studentId, "
			+ "sc.commissionId) "
			+ "from Commission c "
			+ "join StudentCommission sc "
				+ "on c.id = sc.commissionId "
			+ "where c.id = :id "
				+ "and c.startDateTime >= :start")
	List<StudentCommission> findAllByCommissionIdAndActualTime(
			@Param(value = "id") int id, 
			@Param("start") ZonedDateTime startDateTime);
	
	Optional<StudentCommission> findByStudentId(long studentId);
	
	@Query("select new StudentCommission("
			+ "sc.id, "
			+ "sc.studentId, "
			+ "sc.commissionId) "
			+ "from StudentCommission sc "
			+ "join Commission c "
			+ 		"on sc.commissionId = c.id "
			+ "where sc.studentId = :studentId "
			+ 		"and c.startDateTime >= :timestamp")
	Optional<StudentCommission> findByStudentIdAndActualTime(
			@Param("studentId") long studentId, 
			@Param("timestamp") ZonedDateTime startDateTime);
	
	@Query("select new StudentCommission("
			+ "sc.id, "
			+ "sc.studentId, "
			+ "sc.commissionId) "
			+ "from StudentCommission sc "
			+ "join Commission c "
			+ 		"on sc.commissionId = c.id "
			+ "where c.startDateTime >= :timestamp")
	List<StudentCommission> findAllActualRegistrations(
			@Param("timestamp") ZonedDateTime startDateTime);
	
	void deleteAllByStudentId(long studentId);
	
	void deleteAllByCommissionId(int commissionId);
}
