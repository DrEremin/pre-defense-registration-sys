package ru.dreremin.predefense.registration.sys.services.comissions;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ru.dreremin.predefense.registration.sys.dto.requestdto.CommissionDto;
import ru.dreremin.predefense.registration.sys.factories.EntitiesFactory;
import ru.dreremin.predefense.registration.sys.repositories
		 .CommissionRepository;

@RequiredArgsConstructor
@Service
public class CreateCommissionService {

	private final CommissionRepository repository;
	
	@Transactional(isolation = Isolation.SERIALIZABLE)
	public void createComission(CommissionDto dto) {
		repository.save(EntitiesFactory.createComission(dto));
	} 
}
