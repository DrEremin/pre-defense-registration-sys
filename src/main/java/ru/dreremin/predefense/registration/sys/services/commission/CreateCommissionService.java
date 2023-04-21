package ru.dreremin.predefense.registration.sys.services.commission;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import ru.dreremin.predefense.registration.sys.dto.request
		 .CommissionRequestDto;
import ru.dreremin.predefense.registration.sys.repositories
		 .CommissionRepository;
import ru.dreremin.predefense.registration.sys.util.EntitiesFactory;
import ru.dreremin.predefense.registration.sys.util.ZonedDateTimeProvider;

@RequiredArgsConstructor
@Service
public class CreateCommissionService {

	private final CommissionRepository repository;
	
	private final ZonedDateTimeProvider provider;
	
	@Transactional(isolation = Isolation.SERIALIZABLE)
	public void createComission(CommissionRequestDto dto) {
		dto.setStartDateTime(provider.changeTimeZone(dto.getStartDateTime()));
		dto.setEndDateTime(provider.changeTimeZone(dto.getEndDateTime()));
		provider.periodValidation(
				dto.getStartDateTime(), 
				dto.getEndDateTime());
		repository.save(EntitiesFactory.createCommission(dto));
	} 
}
