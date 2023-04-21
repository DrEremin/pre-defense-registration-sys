package ru.dreremin.predefense.registration.sys.services.student;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import ru.dreremin.predefense.registration.sys.dto.response.StudentResponseDto;
import ru.dreremin.predefense.registration.sys.dto.response
		 .WrapperForPageResponseDto;
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
	
	public WrapperForPageResponseDto<Student, StudentResponseDto> 
			getAllStudents(PageRequest pageRequest) {
		
		Page<Student> students = studentRepository
				.findAllOrderByLastName(pageRequest);
		
		return new WrapperForPageResponseDto<>(getPageOfStudentResponseDto(
				students, 
				"Not a single student was found"));
	}
	
	private Map.Entry<Page<Student>, List<StudentResponseDto>> 
			getPageOfStudentResponseDto(
			Page<Student> page, 
			String message) {
		
		List<Student> students = page.getContent();
		List<StudentResponseDto> result = new ArrayList<>(students.size());
		
		if (page.getTotalElements() == 0) {
			throw new EntityNotFoundException(message);
		}
		for (Student student : students) {
			result.add(getStudentResponseDto(student));
		}
		return Map.entry(page, result);
	}
	
	public StudentResponseDto getStudentResponseDto(Student student) {
		
		Person person = personRepository.findById(student.getPersonId())
				.orElseThrow(() -> new EntityNotFoundException(
						"There is no person with such an id"));
		Email email = emailRepository.findByPersonId(person.getId())
				.orElseThrow(() -> new EntityNotFoundException(
						"There is no email with such an person id"));
		Actor actor = actorRepository.findById(person.getActorId())
				.orElseThrow(() -> new EntityNotFoundException(
						"There is no actor with such an id"));
		return new StudentResponseDto(
				actor.getId(),
				person.getLastName(), 
				person.getFirstName(), 
				person.getPatronymic(),
				student.getStudyDirection(),
				student.getStudyType(),
				student.getGroupNumber(),
				email.getAddress(),
				actor.getLogin());
	}
}	
