package ru.dreremin.predefense.registration.sys.repositories;

import java.time.ZonedDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ru.dreremin.predefense.registration.sys.models.Commission;

@Repository
public interface CommissionRepository 
		extends JpaRepository<Commission, Integer> {
	
	List<Commission> findAllByStartDateTimeGreaterThanOrderByStartDateTimeAsc(
			ZonedDateTime currentTimestamp);
	
	@Query("select new Commission("
			+ "c.id, "
			+ "c.startDateTime, "
			+ "c.endDateTime, "
			+ "c.presenceFormat, "
			+ "c.studyDirection,"
			+ "c.location, "
			+ "c.studentLimit) "
			+ "from Commission c "
			+ "where c.startDateTime > :start "
			+ "and c.studyDirection = :direction")
	Page<Commission> findAllActualCommissionsForStudent(
			@Param("start") ZonedDateTime start, 
			@Param("direction") String studyDirection,
			Pageable pageable);
	
	@Query("select new Commission("
			+ "c.id, "
			+ "c.startDateTime, "
			+ "c.endDateTime, "
			+ "c.presenceFormat, "
			+ "c.studyDirection,"
			+ "c.location, "
			+ "c.studentLimit) "
			+ "from Commission c "
			+ "where c.startDateTime > :start")
	Page<Commission> findAllActualCommissionsForTeacher(
			@Param("start") ZonedDateTime start, 
			Pageable pageable);
	
	Page<Commission> findAllByStartDateTimeBetweenOrderByStartDateTime(
			ZonedDateTime startDateTime, 
			ZonedDateTime endDateTime,
			Pageable pageable);
}
