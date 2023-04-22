package ru.dreremin.predefense.registration.sys.controllers.mailing;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import ru.dreremin.predefense.registration.sys.dto.request.MailingRequestDto;
import ru.dreremin.predefense.registration.sys.dto.response.MailingResponseDto;
import ru.dreremin.predefense.registration.sys.services.mailing.MailingService;

//@CrossOrigin(origins = "http://localhost:3002")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/mailing")
public class MailingController {

	private final MailingService service;
	
	@PostMapping(
			value = "/students/all", 
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<MailingResponseDto>> 
			sendMailsToStudents(@Valid @RequestBody MailingRequestDto dto) {
		
		List<MailingResponseDto> responseDto = 
				service.sendMailsToStudents(dto);
		
		log.info("MailingController.sendMailsToStudents() is success");
		return ResponseEntity.ok(responseDto);
	}
	
	@PostMapping(
			value = "/teachers/all",
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<MailingResponseDto>> 
			sendMailsToTeachers(@Valid @RequestBody MailingRequestDto dto) {
		
		List<MailingResponseDto> responseDto = 
				service.sendMailsToTeachers(dto);
		
		log.info("MailingController.sendMailsToTeachers() is success");
		return ResponseEntity.ok(responseDto);
	}
}
