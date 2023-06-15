package ru.dreremin.predefense.registration.sys.services.commission;

import java.time.ZonedDateTime;
import java.util.List;
import javax.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import ru.dreremin.predefense.registration.sys.dto.request
		 .CommissionRequestDto;
import ru.dreremin.predefense.registration.sys.exceptions
		 .EntitiesMismatchException;
import ru.dreremin.predefense.registration.sys.exceptions
		 .NegativeTimePeriodException;
import ru.dreremin.predefense.registration.sys.exceptions.OverLimitException;
import ru.dreremin.predefense.registration.sys.models.Commission;
import ru.dreremin.predefense.registration.sys.models.StudentCommission;
import ru.dreremin.predefense.registration.sys.repositories
		 .CommissionRepository;
import ru.dreremin.predefense.registration.sys.repositories
		 .StudentCommissionRepository;
import ru.dreremin.predefense.registration.sys.util.ZonedDateTimeProvider;

@RequiredArgsConstructor
@Service
public class UpdateCommissionService {

	private final CommissionRepository commissionRepository;
	
	private final StudentCommissionRepository studentCommissionRepository;
	
	private final ZonedDateTimeProvider zonedDateTimeProvider;
	
	@Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = {
			EntityNotFoundException.class,
			NegativeTimePeriodException.class,
			EntitiesMismatchException.class,
			OverLimitException.class})
	public void updateCommission(int id, CommissionRequestDto dto) {
		
		Commission commission = commissionRepository.findById(
				id).orElseThrow(() -> new EntityNotFoundException(
						"Commission with this ID does not exist"));
	
		setAnyFieldsOfCommission(dto, commission);
		commissionRepository.save(commission);
	}
	
	private void setAnyFieldsOfCommission(
			CommissionRequestDto dto, 
			Commission commission) {
		
		setDateTimeFields(
				dto.getStartDateTime(), 
				dto.getEndDateTime(), 
				commission);
		if (dto.getStudyDirection() != null) {
			checkingPossibilityOfUpdateStudyDirection(commission.getId());
			commission.setStudyDirection(dto.getStudyDirection());
		}
		if (dto.getLocation() != null) {
			commission.setLocation(dto.getLocation());
		}
		if (dto.getStudentLimit() != 0) {
			checkingPossibilityOfUpdateStudentLimit(
					commission.getId(), 
					dto.getStudentLimit());
			commission.setStudentLimit(dto.getStudentLimit());
		}
	}
	
	private void setDateTimeFields(
			ZonedDateTime startDateTime,
			ZonedDateTime endDateTime, 
			Commission commission) {
		
		startDateTime = startDateTime != null 
				? zonedDateTimeProvider.changeTimeZone(startDateTime) 
				: null;
		endDateTime = endDateTime != null 
				? zonedDateTimeProvider.changeTimeZone(endDateTime) 
				: null;
		
		if (startDateTime != null && endDateTime != null) {
			zonedDateTimeProvider.periodValidation(startDateTime, endDateTime);
			commission.setStartDateTime(startDateTime);
			commission.setEndDateTime(endDateTime);
		} else if (startDateTime != null && endDateTime == null) {
			zonedDateTimeProvider.periodValidation(
					startDateTime, commission.getEndDateTime());
			commission.setStartDateTime(startDateTime);
		} else if (startDateTime == null && endDateTime != null) {
			zonedDateTimeProvider.periodValidation(
					commission.getStartDateTime(), endDateTime);
			commission.setEndDateTime(endDateTime);
		}
	}
	
	private void checkingPossibilityOfUpdateStudyDirection(int id) {
		
		List<StudentCommission> registrations = studentCommissionRepository
				.findAllByCommissionIdAndActualTime(id, ZonedDateTime.now());
		if (registrations.size() > 0) {
			throw new EntitiesMismatchException(
						"Inconsistency of study direction  between commission and "
						+ "the student");
		}
	}
	
	private void checkingPossibilityOfUpdateStudentLimit(int id, short limit) {
		List<StudentCommission> registrations = studentCommissionRepository
				.findAllByCommissionIdAndActualTime(id, ZonedDateTime.now());
		if (registrations.size() > limit) {
			throw new OverLimitException("Student registration limit is less "
					+ "than the amount of existing registrations");
		}
	}
}
