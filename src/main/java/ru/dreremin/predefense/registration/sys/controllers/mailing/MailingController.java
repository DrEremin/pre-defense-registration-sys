package ru.dreremin.predefense.registration.sys.controllers.mailing;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import ru.dreremin.predefense.registration.sys.dto.request.MailingRequestDto;
import ru.dreremin.predefense.registration.sys.dto.response.MailingResponseDto;
import ru.dreremin.predefense.registration.sys.services.mailing.MailingService;
import ru.dreremin.predefense.registration.sys.util.enums.Role;

//@CrossOrigin(origins = "http://localhost:3002")
@Slf4j
@RequiredArgsConstructor
@RestController
public class MailingController {

	private final MailingService service;
	
	@PostMapping(
			value = "/mailing", 
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<MailingResponseDto>> 
			sendMails(@Valid @RequestBody MailingRequestDto dto) {
		
		List<MailingResponseDto> responseDto;
		
		if (dto.getRecipientRole().equals(Role.STUDENT.getRole())) {
			responseDto = service.sendMailsToStudents(dto);
		} else if (dto.getRecipientRole().equals(Role.TEACHER.getRole())) {
			responseDto = service.sendMailsToTeachers(dto);
		} else {
			responseDto = new ArrayList<>();
		}
		
		log.info("MailingController.sendMails() is success");
		return ResponseEntity.ok(responseDto);
	}
}
