package ru.dreremin.predefense.registration.sys.services.teacher;

import javax.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import ru.dreremin.predefense.registration.sys.dto.request.TeacherRequestDto;
import ru.dreremin.predefense.registration.sys.models.Teacher;
import ru.dreremin.predefense.registration.sys.repositories.TeacherRepository;
import ru.dreremin.predefense.registration.sys.services.person
		 .UpdatePersonService;

@RequiredArgsConstructor
@Service
public class UpdateTeacherService {

	private final TeacherRepository teacherRepository;
	
	private final UpdatePersonService updatePersonService;
	
	@Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = {
			EntityNotFoundException.class})
	public void updateTeacher(long actorId, TeacherRequestDto dto) {
		
		long personId = updatePersonService.updatePerson(actorId, dto);
		
		Teacher teacher = teacherRepository.findByPersonId(personId)
				.orElseThrow(() -> new EntityNotFoundException(
						"Teacher with this user ID does not exist"));
		
		if (dto.getJobTitle() != null) {
			teacher.setJobTitle(dto.getJobTitle());
		}
	}
}
