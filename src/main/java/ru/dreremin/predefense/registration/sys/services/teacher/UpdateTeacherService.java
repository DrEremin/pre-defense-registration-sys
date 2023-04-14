package ru.dreremin.predefense.registration.sys.services.teacher;

import javax.persistence.EntityNotFoundException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ru.dreremin.predefense.registration.sys.dto.request.TeacherRequestDto;
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
public class UpdateTeacherService {

	private final TeacherRepository teacherRepository;
	
	private final ActorRepository actorRepository;
	
	private final PersonRepository personRepository;
	
	private final EmailRepository emailRepository;
	
	private final PasswordEncoder passwordEncoder;
	
	@Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = {
			EntityNotFoundException.class})
	public void updateTeacher(String login, TeacherRequestDto dto) {
		
		Actor actor = actorRepository.findByLogin(login).orElseThrow(
				() -> new EntityNotFoundException(
						"User with this ID does not exist"));
		
		if (dto.getLogin() != null) {
			actor.setLogin(dto.getLogin());
		}
		if (dto.getPassword() != null) {
			actor.setPassword(passwordEncoder.encode(dto.getPassword()));
		}
		
		Person person = personRepository.findByActorId(actor.getId())
				.orElseThrow(() -> new EntityNotFoundException(
						"Person with this login does not exist"));
		
		if (dto.getLastName() != null) {
			person.setLastName(dto.getLastName());
		}
		if (dto.getFirstName() != null) {
			person.setFirstName(dto.getLastName());
		}
		if (dto.getPatronymic() != null) {
			person.setPatronymic(dto.getPatronymic());
		}
		
		Email email = emailRepository.findByPersonId(person.getId())
				.orElseThrow(() -> new EntityNotFoundException(
						"Email for this person does not exist"));
		
		if (dto.getEmail() != null 
				&& !dto.getEmail().equals(email.getAddress())) {
			email.setAddress(dto.getEmail());
		}
		
		Teacher teacher = teacherRepository.findByPersonId(person.getId())
				.orElseThrow(() -> new EntityNotFoundException(
						"Teacher with this login does not exist"));
		
		if (dto.getJobTitle() != null) {
			teacher.setJobTitle(dto.getJobTitle());
		}
	}
}