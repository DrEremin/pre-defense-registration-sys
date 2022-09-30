package ru.dreremin.predefense.registration.sys.services.mailing;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import ru.dreremin.predefense.registration.sys.dto.requestdto.impl.MailingDto;
import ru.dreremin.predefense.registration.sys.dto.responsedto
		 .MailingReportDto;
import ru.dreremin.predefense.registration.sys.exceptions
		 .FailedAuthenticationException;
import ru.dreremin.predefense.registration.sys.models.Email;
import ru.dreremin.predefense.registration.sys.repositories.EmailRepository;
import ru.dreremin.predefense.registration.sys.services.authentication
		 .AuthenticationService;

@RequiredArgsConstructor
@Service
public class MailingService {

	private final JavaMailSender emailSender;
	
	private final AuthenticationService authenticationService;
	
	private final EmailRepository emailRepo;
	
	@Value("${spring.mail.sender.email}")
	private String fromAddress;
	
	private MailingReportDto sendEmail(
			String toAddress, 
			String subject, 
			String content) {
		
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		int status;	
		String message;
		
		simpleMailMessage.setFrom(fromAddress);
		simpleMailMessage.setTo(toAddress);
		simpleMailMessage.setSubject(subject);
		simpleMailMessage.setText(content);
		try {
			emailSender.send(simpleMailMessage);
			status = 200;
			message = "Ok";
		} catch (MailAuthenticationException ea) {
			status = 500;
			message = "Unsuccessful authentication of the "
					+ "sender in the еmail account";
		} catch (MailSendException es) {
			status = 500;
			message = "Failed to send email to current address";
		}
		return new MailingReportDto(status, message, toAddress);
	}
	
	public List<MailingReportDto> sendMailsToStudents(MailingDto dto) 
			throws FailedAuthenticationException {
		
		authenticationService.administratorAuthentication(dto);
		
		List<Email> addresses = emailRepo.findAllByStudent();
		List<MailingReportDto> responseDto = new ArrayList<>(addresses.size());
		
		for (Email email : addresses) {
			responseDto.add(sendEmail(
					email.getBox(), 
					dto.getSubject(), 
					dto.getContent()));
		}
		return responseDto;
	}
	
	public List<MailingReportDto> sendMailsToTeachers(MailingDto dto) 
			throws FailedAuthenticationException {
		
		authenticationService.administratorAuthentication(dto);
		
		List<Email> addresses = emailRepo.findAllByTeacher();
		List<MailingReportDto> responseDto = new ArrayList<>(addresses.size());
		
		for (Email email : addresses) {
			responseDto.add(sendEmail(
					email.getBox(), 
					dto.getSubject(), 
					dto.getContent()));
		}
		return responseDto;
	}
}



