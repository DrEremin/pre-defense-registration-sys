package ru.dreremin.predefense.registration.sys.services.teacher;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import ru.dreremin.predefense.registration.sys.dto.response.TeacherResponseDto;
import ru.dreremin.predefense.registration.sys.dto.response
		 .WrapperForPageResponseDto;
import ru.dreremin.predefense.registration.sys.models.Actor;
import ru.dreremin.predefense.registration.sys.models.Email;
import ru.dreremin.predefense.registration.sys.models.Person;
import ru.dreremin.predefense.registration.sys.models.Teacher;
import ru.dreremin.predefense.registration.sys.repositories.ActorRepository;
import ru.dreremin.predefense.registration.sys.repositories.EmailRepository;
import ru.dreremin.predefense.registration.sys.repositories.PersonRepository;
import ru.dreremin.predefense.registration.sys.repositories.TeacherRepository;

@RequiredArgsConstructor
@Service
public class ReadTeacherService {
	
	private final PersonRepository personRepository;
	
	private final ActorRepository actorRepository;
	
	private final TeacherRepository teacherRepository;
	
	private final EmailRepository emailRepository;
	
	public WrapperForPageResponseDto<Teacher, TeacherResponseDto> 
			getAllTeachers(PageRequest pageRequest) {
		
		Page<Teacher> page = teacherRepository
				.findAllOrderByLastName(pageRequest);
		
		return new WrapperForPageResponseDto<>(
				getPageOfTeacherResponseDto(
						page, 
						"Not a single teacher was found"));
	}
	
	private Map.Entry<Page<Teacher>, List<TeacherResponseDto>> 
			getPageOfTeacherResponseDto(Page<Teacher> page, String message) {
		
		List<Teacher> teachers = page.getContent();
		List<TeacherResponseDto> result = new ArrayList<>(teachers.size());
		/*
		if (page.getTotalElements() == 0) {
			throw new EntityNotFoundException(message);
		}*/
		for (Teacher teacher : teachers) {
			result.add(getTeacherResponseDto(teacher));
		}
		return Map.entry(page, result);
	}
	
	public TeacherResponseDto getTeacherResponseDto(Teacher teacher) {
		
		Person person = personRepository.findById(teacher.getPersonId())
				.orElseThrow(() -> new EntityNotFoundException(
						"There is no person with such an id"));
		Email email = emailRepository.findByPersonId(person.getId())
				.orElseThrow(() -> new EntityNotFoundException(
						"There is no email with such an person id"));
		Actor actor = actorRepository.findById(person.getActorId())
				.orElseThrow(() -> new EntityNotFoundException(
						"There is no actor with such an id"));
		return new TeacherResponseDto(
				actor.getId(),
				person.getLastName(), 
				person.getFirstName(), 
				person.getPatronymic(),
				teacher.getJobTitle(),
				email.getAddress(),
				actor.getLogin());
	}
}








