package ru.dreremin.predefense.registration.sys.services.commission;

import java.time.ZonedDateTime;

import javax.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import ru.dreremin.predefense.registration.sys.dto.request
		 .CommissionRequestDto;
import ru.dreremin.predefense.registration.sys.models.Commission;
import ru.dreremin.predefense.registration.sys.repositories
		 .CommissionRepository;
import ru.dreremin.predefense.registration.sys.repositories.StudentCommissionRepository;
import ru.dreremin.predefense.registration.sys.util.ZonedDateTimeProvider;

@RequiredArgsConstructor
@Service
public class UpdateCommissionService {

	private final CommissionRepository commissionRepository;
	
	private final StudentCommissionRepository studentCommissionRepository;
	
	private final ZonedDateTimeProvider zonedDateTimeProvider;
	
	@Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = {
			EntityNotFoundException.class})
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
		
		if (dto.getStudyDirection() != null) {
			commission.setStudyDirection(dto.getStudyDirection());
		}
		if (dto.getPresenceFormat() != null) {
			commission.setPresenceFormat(dto.getPresenceFormat());
		}
		if (dto.getLocation() != null) {
			commission.setLocation(dto.getLocation());
		}
		if (dto.getStudentLimit() != null) {
			commission.setStudentLimit(dto.getStudentLimit());
		}
		setDateTimeFields(dto, commission);
	}
	
	
	
	private void setDateTimeFields(
			CommissionRequestDto dto, 
			Commission commission) {
		
		ZonedDateTime startDateTime = dto.getStartDateTime();
		ZonedDateTime endDateTime = dto.getEndDateTime();
		
		if (startDateTime != null && endDateTime != null) {
			zonedDateTimeProvider.periodValidation(startDateTime, endDateTime);
			commission.setStartDateTime(zonedDateTimeProvider.changeTimeZone(
					startDateTime));
			commission.setEndDateTime(zonedDateTimeProvider.changeTimeZone(
					endDateTime));
		} else if (startDateTime != null && endDateTime == null) {
			startDateTime = zonedDateTimeProvider.changeTimeZone(
					startDateTime);
			zonedDateTimeProvider.periodValidation(
					startDateTime, commission.getEndDateTime());
			commission.setStartDateTime(startDateTime);
		} else if (startDateTime == null && endDateTime != null) {
			endDateTime = zonedDateTimeProvider.changeTimeZone(endDateTime);
			zonedDateTimeProvider.periodValidation(
					commission.getStartDateTime(), endDateTime);
			commission.setEndDateTime(endDateTime);
		}
	}
}
