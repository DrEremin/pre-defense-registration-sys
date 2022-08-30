package ru.dreremin.predefense.registration.sys.repositories;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.dreremin.predefense.registration.sys.models.Comission;

@Repository
public interface ComissionRepository 
		extends JpaRepository<Comission, Integer> {
	
	List<Comission> findByStartDateTimeGreaterThanOrderByStartDateTimeAsc(
			ZonedDateTime currentTimestamp);
}
