package ru.dreremin.predefense.registration.sys.services.comissions;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import ru.dreremin.predefense.registration.sys.dto.requestdto.impl
		 .ComissionDto;
import ru.dreremin.predefense.registration.sys.factories.EntitiesFactory;
import ru.dreremin.predefense.registration.sys.repositories
		 .ComissionRepository;

@RequiredArgsConstructor
@Service
public class CreateComissionService {

	private final ComissionRepository repository;
	
	@Transactional(isolation = Isolation.SERIALIZABLE)
	public void createComission(ComissionDto dto) {
		repository.save(EntitiesFactory.createComission(dto));
	} 
}
