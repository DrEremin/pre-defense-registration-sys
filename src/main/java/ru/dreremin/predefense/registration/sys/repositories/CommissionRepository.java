package ru.dreremin.predefense.registration.sys.repositories;

import java.time.ZonedDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.dreremin.predefense.registration.sys.models.Commission;

@Repository
public interface CommissionRepository 
		extends JpaRepository<Commission, Integer> {
	
	List<Commission> findAllByStartDateTimeGreaterThanOrderByStartDateTimeAsc(
			ZonedDateTime currentTimestamp);
	
	List<Commission> findAllByStartDateTimeBetweenOrderByStartDateTime(
			ZonedDateTime startDateTime, 
			ZonedDateTime endDateTime);
}
