package ru.dreremin.predefense.registration.sys.services.commission;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ru.dreremin.predefense.registration.sys.dto.request.CommissionDto;
import ru.dreremin.predefense.registration.sys.repositories
		 .CommissionRepository;
import ru.dreremin.predefense.registration.sys.util.EntitiesFactory;

@RequiredArgsConstructor
@Service
public class CreateCommissionService {

	private final CommissionRepository repository;
	
	@Value("${spring.zone}")
	private String zone;
	
	@Transactional(isolation = Isolation.SERIALIZABLE)
	public void createComission(CommissionDto dto) {
		setZone(dto);
		repository.save(EntitiesFactory.createComission(dto));
	} 
	
	private void setZone(CommissionDto dto) {
		
		dto.setStartTimestamp(ZonedDateTime.of(
				dto.getStartTimestamp().toLocalDateTime(), ZoneId.of(zone)));
		dto.setEndTimestamp(ZonedDateTime.of(
				dto.getEndTimestamp().toLocalDateTime(), ZoneId.of(zone)));
	}
}
