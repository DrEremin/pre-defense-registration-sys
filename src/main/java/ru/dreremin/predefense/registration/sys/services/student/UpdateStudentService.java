package ru.dreremin.predefense.registration.sys.services.student;

import java.time.ZonedDateTime;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import ru.dreremin.predefense.registration.sys.dto.request.StudentRequestDto;
import ru.dreremin.predefense.registration.sys.exceptions
		 .EntitiesMismatchException;
import ru.dreremin.predefense.registration.sys.exceptions
		 .NotReadableRequestParameterException;
import ru.dreremin.predefense.registration.sys.models.Student;
import ru.dreremin.predefense.registration.sys.models.StudentCommission;
import ru.dreremin.predefense.registration.sys.repositories
		 .StudentCommissionRepository;
import ru.dreremin.predefense.registration.sys.repositories.StudentRepository;
import ru.dreremin.predefense.registration.sys.services.person
		 .UpdatePersonService;

@RequiredArgsConstructor
@Service
public class UpdateStudentService {

	private final StudentRepository studentRepository;
	
	private final StudentCommissionRepository studentCommissionRepository;
	
	private final UpdatePersonService updatePersonService;
	
	@Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = {
			EntityNotFoundException.class, 
			NotReadableRequestParameterException.class,
			EntitiesMismatchException.class})
	public void updateStudent(long actorId, StudentRequestDto dto) {

		Student student = studentRepository.findByActorId(actorId)
				.orElseThrow(() -> new EntityNotFoundException(
						"Student with this user ID does not exist"));
		
		updatePersonService.updatePerson(actorId, dto);
		
		if (dto.getGroup() != null) {
			student.setGroupNumber(dto.getGroup());
		}
		if (dto.getStudyDirection() != null) {
			checkingPossibilityOfUpdateStudyDirection(student.getId());
			student.setStudyDirection(dto.getStudyDirection());
		}
		if (dto.getStudyType() != null) {
			student.setStudyType(dto.getStudyType());
		}
	}
	
	private void checkingPossibilityOfUpdateStudyDirection(long id) {
		
		Optional<StudentCommission> studentCommissionOpt = 
				studentCommissionRepository.findByStudentIdAndActualTime(
						id, ZonedDateTime.now());
		
		if (studentCommissionOpt.isPresent()) {
			throw new EntitiesMismatchException(
					"Inconsistency of study direction  between commission and "
					+ "the student");
		}
	}
}
