package ru.dreremin.predefense.registration.sys.services.teacher;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.dreremin.predefense.registration.sys.dto.response.TeacherDto;
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
	
	public List<TeacherDto> getAllTeachers() {
		
		List<Teacher> teachers = teacherRepository.findAll();
		List<TeacherDto> result = new ArrayList<>();
		
		if (teachers.size() == 0) {
			throw new EntityNotFoundException(
					"Not a single teacher was found");
		}
		
		for (Teacher teacher : teachers) {
			result.add(getTeacherDto(teacher));
		}
		return result;
	}
	
	private TeacherDto getTeacherDto(Teacher teacher) {
		
		Person person = personRepository.findById(teacher.getPersonId())
				.orElseThrow(() -> new EntityNotFoundException(
						"There is no person with such an id"));
		Email email = emailRepository.findByPersonId(person.getId())
				.orElseThrow(() -> new EntityNotFoundException(
						"There is no email with such an person id"));
		Actor actor = actorRepository.findById(person.getActorId())
				.orElseThrow(() -> new EntityNotFoundException(
						"There is no actor with such an id"));
		return new TeacherDto(
				person.getLastName(), 
				person.getFirstName(), 
				person.getPatronymic(),
				teacher.getJobTitle(),
				email.getBox(),
				actor.getLogin());
	}
}








