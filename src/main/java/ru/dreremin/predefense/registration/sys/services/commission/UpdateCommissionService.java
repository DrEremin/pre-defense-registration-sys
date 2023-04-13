package ru.dreremin.predefense.registration.sys.services.commission;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import javax.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import ru.dreremin.predefense.registration.sys.dto.request.CommissionDto;
import ru.dreremin.predefense.registration.sys.models.Commission;
import ru.dreremin.predefense.registration.sys.repositories
		 .CommissionRepository;

@RequiredArgsConstructor
@Service
public class UpdateCommissionService {

	private final CommissionRepository commissionRepository;
	
	@Value("${spring.zone}")
	private String zone;
	
	@Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = {
			EntityNotFoundException.class})
	public void updateCommission(CommissionDto dto) {
		
		Commission commission = commissionRepository.findById(
				dto.getId()).orElseThrow(() -> new EntityNotFoundException(
						"Commission with this id does not exist"));
		
		setZone(dto);
		setFieldsOfCommission(dto, commission);
		commissionRepository.save(commission);
	}
	
	private void setFieldsOfCommission(
			CommissionDto dto, 
			Commission commission) {
		
		if (dto.getStartDateTime() != null) {
			commission.setStartDateTime(dto.getStartDateTime());
		}
		if (dto.getEndDateTime() != null) {
			commission.setEndDateTime(dto.getEndDateTime());
		}
		if (dto.getStudyDirection() != null) {
			commission.setStudyDirection(dto.getStudyDirection());
		}
		if (dto.getPresenceFormat() != null) {
			commission.setPresenceFormat(dto.getPresenceFormat());
		}
		if (dto.getLocation() != null) {
			commission.setLocation(dto.getLocation());
		}
		if (dto.getStudentLimit() != 0) {
			commission.setStudentLimit(dto.getStudentLimit());
		}
	}
	
	private void setZone(CommissionDto dto) {
		
		if (dto.getStartDateTime() != null) {
			dto.setStartDateTime(ZonedDateTime.of(
					dto.getStartDateTime().toLocalDateTime(), ZoneId.of(zone)));
		}
		if (dto.getEndDateTime() != null) {
			dto.setEndDateTime(ZonedDateTime.of(
					dto.getEndDateTime().toLocalDateTime(), ZoneId.of(zone)));
		}
	}
}
