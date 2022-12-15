package ru.dreremin.predefense.registration.sys.controllers.mailing;

import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.dreremin.predefense.registration.sys.dto.requestdto.MailingDto;
import ru.dreremin.predefense.registration.sys.dto.responsedto
		 .MailingReportDto;
import ru.dreremin.predefense.registration.sys.exceptions
		 .FailedAuthenticationException;
import ru.dreremin.predefense.registration.sys.services.mailing.MailingService;

//@CrossOrigin(origins = "http://localhost:3002")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/mailing")
public class MailingController {

	private final MailingService service;
	
	@PostMapping("/students")
	public List<MailingReportDto> sendMailsToStudents(
			@Valid @RequestBody MailingDto dto) {
		
		List<MailingReportDto> responseDto = service.sendMailsToStudents(dto);
		
		log.info("MailingController.sendMailsToStudents() is success");
		return responseDto;
	}
	
	@PostMapping("/teachers")
	public List<MailingReportDto> sendMailsToTeachers(
			@Valid @RequestBody MailingDto dto) {
		
		List<MailingReportDto> responseDto = service.sendMailsToTeachers(dto);
		
		log.info("MailingController.sendMailsToTeachers() is success");
		return responseDto;
	}
}
