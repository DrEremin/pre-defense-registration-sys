package ru.dreremin.predefense.registration.sys.services.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import ru.dreremin.predefense.registration.sys.dto.response
		 .AdministratorResponseDto;
import ru.dreremin.predefense.registration.sys.dto.response
		 .WrapperForPageResponseDto;
import ru.dreremin.predefense.registration.sys.models.Actor;
import ru.dreremin.predefense.registration.sys.repositories.ActorRepository;

@RequiredArgsConstructor
@Service
public class ReadAdministratorService {
	
	private final ActorRepository actorRepository;
	
	public WrapperForPageResponseDto<Actor, AdministratorResponseDto> 
			getAllAdministrators(PageRequest pageRequest) {
		
		Page<Actor> page = actorRepository.findAllAdministrators(pageRequest);
		
		return new WrapperForPageResponseDto<>(
				getPageOfAdministratorResponseDto(
						page, "Not a single administrator was found"));
	}
	
	private Map.Entry<Page<Actor>, List<AdministratorResponseDto>> 
			getPageOfAdministratorResponseDto(
					Page<Actor> page, 
					String message) {

		List<Actor> actors = page.getContent();
		List<AdministratorResponseDto> result = new ArrayList<>(actors.size());

		if (page.getTotalElements() == 0) {
			throw new EntityNotFoundException(message);
		}
		for (Actor actor : actors) {
			result.add(getAdministratorResponseDto(actor));
		}
		return Map.entry(page, result);
	}

	private AdministratorResponseDto getAdministratorResponseDto(Actor actor) {
	return new AdministratorResponseDto(actor.getId(), actor.getLogin());
	}
}
