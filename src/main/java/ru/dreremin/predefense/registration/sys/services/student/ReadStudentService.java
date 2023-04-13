package ru.dreremin.predefense.registration.sys.services.student;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import ru.dreremin.predefense.registration.sys.dto.response.StudentDto;
import ru.dreremin.predefense.registration.sys.models.Actor;
import ru.dreremin.predefense.registration.sys.models.Email;
import ru.dreremin.predefense.registration.sys.models.Person;
import ru.dreremin.predefense.registration.sys.models.Student;
import ru.dreremin.predefense.registration.sys.repositories.ActorRepository;
import ru.dreremin.predefense.registration.sys.repositories.EmailRepository;
import ru.dreremin.predefense.registration.sys.repositories.PersonRepository;
import ru.dreremin.predefense.registration.sys.repositories.StudentRepository;

@RequiredArgsConstructor
@Service
public class ReadStudentService {
	
	private final PersonRepository personRepository;
	
	private final ActorRepository actorRepository;
	
	private final StudentRepository studentRepository;
	
	private final EmailRepository emailRepository;
	
	public List<StudentDto> getAllStudents() {
		
		List<Student> students = studentRepository.findAll();
		List<StudentDto> result = new ArrayList<>();
		
		if (students.size() == 0) {
			throw new EntityNotFoundException(
					"Not a single student was found");
		}
		
		for (Student student : students) {
			result.add(getStudentDto(student));
		}
		return result;
	}
	
	private StudentDto getStudentDto(Student student) {
		
		Person person = personRepository.findById(student.getPersonId())
				.orElseThrow(() -> new EntityNotFoundException(
						"There is no person with such an id"));
		Email email = emailRepository.findByPersonId(person.getId())
				.orElseThrow(() -> new EntityNotFoundException(
						"There is no email with such an person id"));
		Actor actor = actorRepository.findById(person.getActorId())
				.orElseThrow(() -> new EntityNotFoundException(
						"There is no actor with such an id"));
		return new StudentDto(
				person.getLastName(), 
				person.getFirstName(), 
				person.getPatronymic(),
				student.getStudyDirection(),
				student.getStudyType(),
				student.getGroupNumber(),
				email.getBox(),
				actor.getLogin());
	}

}
