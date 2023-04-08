package ru.dreremin.predefense.registration.sys.services.commission;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ru.dreremin.predefense.registration.sys.models.Commission;
import ru.dreremin.predefense.registration.sys.repositories.CommissionRepository;
import ru.dreremin.predefense.registration.sys.repositories.NoteRepository;
import ru.dreremin.predefense.registration.sys.repositories.StudentCommissionRepository;
import ru.dreremin.predefense.registration.sys.repositories.TeacherCommissionRepository;

@RequiredArgsConstructor
@Service
public class DeleteCommissionService {
	
	private final CommissionRepository commissionRepo;
	
	private final NoteRepository noteRepo;
	
	private final TeacherCommissionRepository teacherCommissionRepo;
	
	private final StudentCommissionRepository studentCommissionRepo;

	@Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = {
			EntityNotFoundException.class})
	public void deleteComission(int commissionId) {
		
		Optional<Commission> commissionOpt = commissionRepo.findById(
				commissionId);
		if (commissionOpt.isEmpty()) {
			throw new EntityNotFoundException(
					"There is not exists comission with this Id");
		}
		noteRepo.deleteByCommissionId(commissionId);
		teacherCommissionRepo.deleteAllByCommissionId(commissionId);
		studentCommissionRepo.deleteAllByCommissionId(commissionId);
		commissionRepo.deleteById(commissionId);
	}
}
