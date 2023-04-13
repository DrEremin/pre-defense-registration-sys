package ru.dreremin.predefense.registration.sys.services.commission;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.dreremin.predefense.registration.sys.repositories.CommissionRepository;

@RequiredArgsConstructor
@Service
public class UpdateCommissionService {

	private final CommissionRepository commissionRepository;
	
	public void updateCommission() {
		
	}
}
